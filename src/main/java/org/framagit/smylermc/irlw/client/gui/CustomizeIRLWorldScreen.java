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


package org.framagit.smylermc.irlw.client.gui;

import java.util.Random;

import org.framagit.smylermc.irlw.client.gui.widget.DynamicOptionSlider;
import org.framagit.smylermc.irlw.client.gui.widget.SimpleMapWidget;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings;
import org.framagit.smylermc.irlw.world.gen.IRLWGenerationSettings.InvalidSettingsException;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.TranslationTextComponent;


/**
 * 
 * 
 * @author Smyler
 *
 */
public class CustomizeIRLWorldScreen extends Screen{
	
	
	private CreateWorldScreen createWorldGui;
	
	private IRLWGenerationSettings generationSettings;
			
	private TextFieldWidget	 generatorJson;
	private TextFieldWidget longitudeField;
	private TextFieldWidget latitudeField;
	private DynamicOptionSlider zoomSlider;
	
	private SimpleMapWidget smap;
	private Button mapButton;
	

	/**
	 * Constructor
	 * 
	 * @param mc
	 * @param guiCreateWorld
	 */
    public CustomizeIRLWorldScreen(Minecraft mc, CreateWorldScreen guiCreateWorld) {
    	super(new TranslationTextComponent("irlwworldgui.title"));
		this.minecraft = mc;
		this.createWorldGui = guiCreateWorld;
		try {
			this.generationSettings = new IRLWGenerationSettings(this.createWorldGui.chunkProviderSettingsJson);
		} catch(IRLWGenerationSettings.InvalidSettingsException e) {
			
		}
		
	}


	/**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void init(){
    	
    	this.minecraft.keyboardListener.enableRepeatEvents(true); //Easier to type with this activated.
        this.buttons.clear();
        
        //Done button
        this.addButton(new Button(this.width / 2 - 75, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0]), this::done));
        
        generatorJson = new TextFieldWidget(this.font, this.width / 2 - 20, 40, 205, 20, I18n.format("irlwworldgui.json"));
        generatorJson.setMaxStringLength(500);
        this.children.add(this.generatorJson);
        
        this.zoomSlider = new DynamicOptionSlider(this.minecraft.gameSettings, this.width / 2 - 175, 70, 200, 20,
        		new SliderPercentageOption (
        				I18n.format("irlwworldgui.zoom_level"), 0, IRLWGenerationSettings.MAX_ZOOM_LEVEL, 1,
        				this::getZoomLevel,
        				this::setZoomLevel,
        				(setting, option) -> I18n.format("irlwworldgui.zoom_level") + this.generationSettings.getZoomLevel()
        			)
        	);
        this.addButton(zoomSlider);
        
        longitudeField = new TextFieldWidget(this.font, this.width / 2 + 15, 100, 170, 20, I18n.format("irlwworldgui.spawn_long"));
        latitudeField = new TextFieldWidget(this.font, this.width / 2 + 15, 130, 170, 20, I18n.format("irlwworldgui.spawn_lat"));
        longitudeField.setMaxStringLength(20);
        latitudeField.setMaxStringLength(20);
        longitudeField.setText("" + this.generationSettings.getSpawnLong());
        latitudeField.setText("" + this.generationSettings.getSpawnLat());
        this.children.add(this.longitudeField);
        this.children.add(this.latitudeField);
        
        this.mapButton = new Button(this.width/2 - 160, 100, 50, 50, "", this::letUserPickCoordinates);
        this.addButton(mapButton);
        this.smap = new SimpleMapWidget(this.width / 2 - 170, 100, 60);
        this.smap.showPointer = true;
        this.addButton(this.smap);

        this.addButton(new Button(this.width/2 - 160, 170, 150, 20, I18n.format("irlwworldgui.randomize_spawn"), this::populateRandomCoordinates));

        Button presetButton = new Button(this.width/2 + 10, 170, 150, 20, I18n.format("irlwworldgui.preset_spawn"), null); //TODO Implement preset spawn points
        presetButton.active = false;
        this.addButton(presetButton);
        
        this.updateJsonFromValues();
        
    }
    
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
    	this.renderBackground();
    	super.render(mouseX, mouseY, partialTicks);
    	this.generatorJson.renderButton(mouseX, mouseY, partialTicks);
    	this.longitudeField.renderButton(mouseX, mouseY, partialTicks);
    	this.latitudeField.renderButton(mouseX, mouseY, partialTicks);
    	this.drawCenteredString(this.font, I18n.format("irlwworldgui.title", new Object[0]), this.width / 2, 20, -1);
    	this.drawString(this.font, I18n.format("irlwworldgui.json"), this.width / 2 - 160, 46, -1);
    	this.drawString(this.font, I18n.format("irlwworldgui.spawn_long"), this.width / 2 - 100, 106, -1);
    	this.drawString(this.font, I18n.format("irlwworldgui.spawn_lat"), this.width / 2 - 100, 136, -1);
    	long worldSize = 1<<(this.generationSettings.getZoomLevel() + 8);
    	this.drawCenteredString(this.font, I18n.format("irlwworldgui.world_size", worldSize), this.width / 2 + 98, 76, 0xFFFFFFFF);
    	this.smap.setPointerLongLat(this.generationSettings.getSpawnLong(), this.generationSettings.getSpawnLat(), 0xFFFF0000);
    	if(this.mapButton.isMouseOver(mouseX, mouseY)) {
    		//TODO Implement dynamic map
    		//this.renderTooltip(I18n.format("irlwworldgui.spawn_picking"), mouseX, mouseY);
    	}
    }
    

    @Override
    public void tick() {
    	super.tick();
    	this.generatorJson.tick();
    	this.longitudeField.tick();
    	this.latitudeField.tick();
    	if(this.generatorJson.isFocused()) {
    		this.updateValuesFromJson();
    	}
    	if(this.longitudeField.isFocused()) {
    		try {
    			this.generationSettings.setSpawnLong(Double.parseDouble(this.longitudeField.getText()));
    			this.longitudeField.setTextColor(0xFFFFFF);
    			this.updateJsonFromValues();
    		}catch(NumberFormatException | InvalidSettingsException e) {
    			this.longitudeField.setTextColor(0xFF3000);
    		}
    	}
    	if(this.latitudeField.isFocused()){
    		try {
    			this.generationSettings.setSpawnLat(Double.parseDouble(this.latitudeField.getText()));
    			this.latitudeField.setTextColor(0xFFFFFF);
    			this.updateJsonFromValues();
    		}catch(NumberFormatException | InvalidSettingsException e) {
    			this.latitudeField.setTextColor(0xFF3000);
   			}
    	}
    }
    
    
    
    /**
     * Called when the done button is pressed
     * 
     * @param b
     */
    private void done(Button b) {
    	this.save();
		this.minecraft.displayGuiScreen(this.createWorldGui) ;
    }
    
    
    private void populateRandomCoordinates(Button b) {
    	this.populateRandomCoordinates();
    }
    /**
     * Called when the random spawn point button is pressed
     * 
     */
    public void populateRandomCoordinates() {
		Random random = new Random();
		try {
			this.generationSettings.setSpawnLat((random.nextDouble() - .5) * 170);
			this.generationSettings.setSpawnLong((random.nextDouble() - .5) * 360);
		} catch (InvalidSettingsException e) {
			//It should never happen if the random values are generated properly
			e.printStackTrace();
		}
		this.latitudeField.setText("" + this.generationSettings.getSpawnLat());
		this.longitudeField.setText("" + this.generationSettings.getSpawnLong());
		this.updateJsonFromValues();
		this.longitudeField.setTextColor(0xFFFFFF);
		this.latitudeField.setTextColor(0xFFFFFF);
    }
    
    
    
    /**
     * Called when the minimap is pressed to let user pick a position on a dynmap
     * 
     * @param b
     */
    private void letUserPickCoordinates(Button b) {
    	//TODO Implement
    	//Minecraft.getInstance().displayGuiScreen(new PickLocationScreen(this));
    }
    
    
    
    /**
     * Saves the gui's settings to the configuration object and then saves it.
     */
    private void save(){
    	this.createWorldGui.chunkProviderSettingsJson = this.generationSettings.toNBT(); 
    }
    
    
    /**
     * Called by the game when the mouse is clicked, has to be passed to the components of the gui to have them react,
     * and to super for automatic button process
     */
    @Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
    	this.generatorJson.mouseClicked(mouseX, mouseY, mouseButton);
    	this.latitudeField.mouseClicked(mouseX, mouseY, mouseButton);
    	this.longitudeField.mouseClicked(mouseX, mouseY, mouseButton);
    	return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called by the game when a key is pressed, the event has to be passed to the guis's components if the focus is on them.
     */
    @Override
	public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_){
    	return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
    }
	
	public void updateJsonFromValues() {
        generatorJson.setText(this.generationSettings.toJson());
	}
	
	public void updateValuesFromJson() {
		try{
			this.generationSettings = new IRLWGenerationSettings(this.generatorJson.getText());
    	}catch(Exception e){
    		this.generatorJson.setTextColor(0xFF3000);
    		return;
    	}
		
		this.generatorJson.setTextColor(0xFFFFFF);
		
		this.longitudeField.setText("" + this.generationSettings.getSpawnLong());
		this.latitudeField.setText("" + this.generationSettings.getSpawnLat());
		
		this.zoomSlider.updateValue();
		
	}
	
	private double getZoomLevel(GameSettings settings) {
		return this.generationSettings.getZoomLevel();
	}

	private double setZoomLevel(GameSettings settings, double zoomLevel) {
		try {
			this.generationSettings.setZoomLevel((int) zoomLevel);
		} catch (InvalidSettingsException e) {
			//It should never happen if the slider has been set correctly
			e.printStackTrace();
		}
		this.updateJsonFromValues();
		return this.generationSettings.getZoomLevel();
	}
	
}
