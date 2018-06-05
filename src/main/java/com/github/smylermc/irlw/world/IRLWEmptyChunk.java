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

	File created on 7 mai 2018 
*/
package com.github.smylermc.irlw.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * @author SmylerMC
 *
 * The goal of this class is to have a chunk which is not saved by the game,
 * so that it can be regenerated after reloading the world.
 * Something similar exists in vanilla: net.minecraft.world.chunk.EmptyChunk, 
 * but for some reasons it crashes the game if a fluid floods into it,
 * throwing a StackOverflowException .
 * I think it is because fluids try again and again to flood if they fail,
 * so the difference between this and vanilla empty chunk is that this one handles everything normally,
 * but is never saved
 *
 */
public class IRLWEmptyChunk extends Chunk{

	/**
	 * @param worldIn
	 * @param primer
	 * @param x
	 * @param z
	 */
	public IRLWEmptyChunk(World worldIn, int x, int z) {
		super(worldIn, x, z);
	}
	
	@Override
	public void markDirty() {} //We do not need to save this chunk

    /**
     * Generates the height map for a chunk from scratch
     */
    @Override
    protected void generateHeightMap()
    {
        super.generateHeightMap(); // Marks the chunk as dirty, we don't want that
        super.setModified(false);
    }

    /**
     * Returns false because this Chunk should not be saved
     */
    @Override
    public boolean needsSaving(boolean weDontCareOfThatParameter){
        return false;
    }

    @Override
    public boolean isEmpty(){
        return true;
    }

    /**
     * No need to waist time with this
     * 
     */
    @Override
    public void populate(IChunkProvider chunkProvider, IChunkGenerator chunkGenrator){}
    @Override
    protected void populate(IChunkGenerator generator){}

}
