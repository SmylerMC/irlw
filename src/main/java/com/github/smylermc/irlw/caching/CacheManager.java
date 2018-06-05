/* IRL World Minecraft Mod
    Copyright (C) 2017  Smyler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program. If not, see <http://www.gnu.org/licenses/>.

	The author can be contacted at smyler@mail.com
*/
package com.github.smylermc.irlw.caching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.github.smylermc.irlw.IRLW;
import com.github.smylermc.irlw.maps.exceptions.InvalidMapboxSessionException;
import com.github.smylermc.irlw.maps.tiles.RasterWebTile;
import com.github.smylermc.irlw.maps.tiles.tiles.MapboxWebTile;
import com.github.smylermc.irlw.maps.utils.MapboxUtils;
import com.google.common.io.Files;


//FIXME If we loose Internet access, the thread just gets stuck...


/**
 * @author Smyler
 * 
 * This class is responsible for handling all resources which needs to be downloaded at some point
 * It has a worker thread running to allow for asynchronous caching.
 * The worker is started by the mod during preinit
 *
 * FIXME Crashes when clicking on ignore
 */
public class CacheManager implements Runnable {
	
	private Thread worker;
	private volatile boolean workerRunning;
	private LinkedList<Cachable> toCacheAsync = new LinkedList<Cachable>();
	
	private File cachingDirectory;
	
	private Cachable currentlyCachedByWorker = null;
	
	
	public CacheManager(String path) throws IOException {
		this.setCachingDirectory(new File(path));
		this.createWorker();
	}

	
	public CacheManager() {
		this.cachingDirectory = Files.createTempDir();
		this.createWorker();
	}
	
	
	private void createWorker() {
		this.worker = new Thread(this);
		this.worker.setName("IRLW Caching Thread");
		this.worker.setDaemon(true);
	}
	
	/**
	 * Called by the worker when it is launched.
	 * If called by an other thread, it calls CacheManager::startWorker and returns. It should be avoided
	 */
	@Override
	public void run() {
		if(! this.isCallingFromWorker()) {
			this.startWorker();
			return;
		}
		this.workerRunning = true;
		synchronized(IRLW.logger) {
			IRLW.logger.info("Started IRLW cache manager.");
		}
		while(this.workerRunning) {
			Cachable toCache;
			int sleep = 10;
			synchronized(this.toCacheAsync) {
				toCache = this.toCacheAsync.poll();
			}
			if(toCache == null) {
				sleep = 10;
			}else {
				sleep = 10;
				synchronized(toCache) {  //Crashes the thread when null
					try {
						this.currentlyCachedByWorker = toCache;
						this.cache(toCache);
						this.currentlyCachedByWorker = null;
					}catch(IOException e) {
						synchronized(IRLW.logger) {
							IRLW.logger.error("Failed to cache a file, you may not be connected to the internet, logging exception.");
							IRLW.logger.catching(e);
							IRLW.proxy.failedToCache(); //TODO implement this in the proxys
						}
					}
					catch (InvalidMapboxSessionException e) {
						synchronized(IRLW.logger) {
							IRLW.logger.error("Failed to cache a file due to a forbiden response from the Mapbox API");
						}
						IRLW.proxy.onInInvalidMapboxToken(); //TODO implement on proxys
					}
				}
				try {Thread.sleep(sleep);} catch (InterruptedException e) {IRLW.logger.catching(e);}
			}
		}
		synchronized(IRLW.logger) {
			IRLW.logger.info("Stopping IRLW cache manager.");
		}
	}
	
	/**
	 * Starts the worker if it is not running
	 * Does nothing if it is
	 */
	public void startWorker() {
		if(!this.worker.isAlive()) this.worker.start();
	}
	
	/**
	 * Stops the worker if it is running
	 * Does nothing if it isn't
	 */
	public void stopWorker() {
		if(this.worker.isAlive()) this.workerRunning = false;
	}
	
	/**
	 * 
	 * @return True if the worker is running
	 */
	public boolean isWorkerAlive() {
		return this.worker.isAlive();
	}
	
	/**
	 * 
	 * @return true when called from the worker, false otherwise.
	 */
	public boolean isCallingFromWorker() {
		return Thread.currentThread().equals(this.worker);
	}
	
	/**
	 * @return The directory where cached files are saved as a File object
	 */
	public File getCachingDirectory() {
		return cachingDirectory;
	}
	
	/**
	 * @return The path to the directory where cached files are saved as a File string
	 */
	public String getCachingPath() {
		return cachingDirectory.getAbsolutePath();
	}


	/**
	 * Sets directory where cached files are saved
	 * 
	 * @param path to a directory as a string
	 * @throws IOException if the path is not an actual directory
	 */
	public void setCachingDirectory(String path) throws IOException {
		this.setCachingDirectory(new File(path));
	}
	
	/**
	 * Sets directory where cached files are saved
	 * 
	 * @param a File object representing the directory
	 * @throws IOException if the file is not an actual directory
	 */
	public void setCachingDirectory(File cachingDirectory) throws IOException {
		if(!cachingDirectory.exists() && cachingDirectory.isDirectory()) {
			throw new IOException("No such file or directory: " + cachingDirectory.getPath());
		}
		this.cachingDirectory = cachingDirectory;
	}
	
	
	
	/**
	 * Caches a Cachable and returns when done
	 * 
	 * @param toCache
	 * @throws IOException
	 * @throws InvalidMapboxSessionException
	 */
	public void cache(Cachable toCache) throws IOException, InvalidMapboxSessionException {
		if(!this.isCallingFromWorker()) 
			IRLW.logger.warn("Caching from an other thread!!");
		
		if(this.isCached(toCache)) return;
		File f = this.getCachableFile(toCache);
		if(toCache instanceof MapboxWebTile) {
			try {
				MapboxUtils.saveMapboxUrlToFile(toCache.getURL(), f);
			}catch(FileNotFoundException e) {
			} //It means the tile is blank, not that it does not exist.
		}else {
			//FileUtils.copyURLToFile(toCache.getURL(), f);
			this.downloadUrlToFile(toCache.getURL(), f);
		}
		if(!f.exists() || !f.isFile()) {
			if(toCache instanceof RasterWebTile) {
				IRLW.logger.warn("Failed to cache a file...");
			}else {
				synchronized(IRLW.logger) {
					IRLW.logger.warn("Failed to cache a file...");
				}
			}
		}
		toCache.cached(f);
	}
	
	/**
	 * Adds the Cachable to a queue so that it gets cached by the worker in a near future
	 * 
	 * @param toCache
	 */
	public void cacheAsync(Cachable toCache) {
		synchronized(this.toCacheAsync) {
			this.toCacheAsync.add(toCache);
		}
	}
	

	private File getCachableFile(Cachable c) {
		return new File(this.cachingDirectory.getAbsoluteFile() + "/" + c.getFileName());
	}
	
	
	/**
	 * @param c The resource to check
	 * 
	 * @return true if the given resource has been cached already
	 */
	public boolean isCached(Cachable c) {
		File f = this.getCachableFile(c);
		return f.exists() && f.isFile() && !c.equals(this.currentlyCachedByWorker);
	}
	
	
	/**
	 * Return the file from the cachable.
	 * If the file does not exist but no error has been thrown, such as with empty tiles, returns null
	 * Downloads the resource if needed, or read it from disk.
	 * 
	 * @param c
	 * @throws InvalidMapboxSessionException 
	 * @throws IOException 
	 */
	public File getFile(Cachable c) throws IOException, InvalidMapboxSessionException {
		File f = this.getCachableFile(c);
		if(f.exists() && f.isFile()) return f;
		this.cache(c);
		if(f.exists() && f.isFile()) return f;
		return null;
	}
	
	/**
	 * Empties the queue of resources waiting to be cached by the worker
	 */
	public void clearQueue() {
		synchronized(this.toCacheAsync) {
			this.toCacheAsync = new LinkedList<Cachable>();
		}
	}
	
	/**
	 * 
	 * @return The number of resources waiting to be cached by the worker
	 */
	public int getQueueSize() {
		synchronized(this.toCacheAsync) {
			return this.toCacheAsync.size();
		}
	}
	
	
	public void createDirectory() {
		this.cachingDirectory.mkdirs();
	}
	
	public boolean isBeingCached(Cachable c) {
		if(c.equals(this.currentlyCachedByWorker))
			return true;
		synchronized(this.toCacheAsync) {
			for(Cachable ca: this.toCacheAsync) {
				if(ca.getURL().equals(c.getURL())) return true;
			}
		}
		return false;		
	}
	

	public int downloadUrlToFile(URL url, File file) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setAllowUserInteraction(false);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", IRLW.HTTP_USER_AGENT);
		
		connection.connect();
		
		switch(connection.getResponseCode()) {
		
		case HttpURLConnection.HTTP_OK:  //TODO Make sure we don't need to support 304 response (could they happend in this context?)
			InputStream inStream = connection.getInputStream();
			OutputStream outStream = new FileOutputStream(file);
			int lastByte = -1;
            byte[] buffer = new byte[32];
            while ((lastByte = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, lastByte);
            }
			break;
		case HttpURLConnection.HTTP_NOT_FOUND:
			throw new FileNotFoundException();
		}
		return connection.getResponseCode();
	}

}
