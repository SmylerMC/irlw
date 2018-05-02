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


package com.github.smylermc.irlw.client.gui;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;


/**
 * This will change a lot in the future, as it is not the current main focus
 * 
 * @author Smyler
 *
 */
public class GuiCustomizeIRLWorld extends GuiScreen implements GuiPageButtonList.GuiResponder{
	
	
	private GuiCreateWorld createWorldGui;
	private Minecraft mc;
	
	private static final int
		DONE_ID = 0,
		JSON_ID = 1,
		ZOOM_ID = 2,
		PICK_ID = 3,
		LONG_ID = 4,
		LATI_ID = 5,
		RAND_ID = 5,
		PRESET_ID = 5;
			
	private GuiTextField generatorJson;
	private GuiTextField longitudeField;
	private GuiTextField latitudeField;
	
	private double spawnLong = 0;
	private double spawnLat = 0;
	
	private GuiSimpleMap smap;
	private GuiButton mapButton;
	private int zoomLevel;
	

	/**
	 * Constructor
	 * 
	 * @param mc
	 * @param guiCreateWorld
	 */
    public GuiCustomizeIRLWorld(Minecraft mc, GuiCreateWorld guiCreateWorld) {
		this.mc = mc;
		this.createWorldGui = guiCreateWorld;
	}


	/**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui(){
    	
    	Keyboard.enableRepeatEvents(true); //Easier to type with this activated.
        this.buttonList.clear();
        
        //Done button
        this.buttonList.add(new GuiButton(DONE_ID, this.width / 2 - 75, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        generatorJson = new GuiTextField(JSON_ID, this.fontRenderer, this.width / 2 - 50, 40, 200, 20);
        generatorJson.setMaxStringLength(500);
        
        this.buttonList.add(new GuiSlider(this, ZOOM_ID, this.width / 2 - 175, 70, "slider", 0f, 20f, (float) this.zoomLevel, new GuiSlider.FormatHelper() {

			@Override
			public String getText(int id, String name, float value) {
				return I18n.format("irlwworldgui.zoom_level") + ": " + zoomLevel;
			}
			
		}));
        
        longitudeField = new GuiTextField(LONG_ID, this.fontRenderer, this.width / 2 + 15, 100, 170, 20);
        latitudeField = new GuiTextField(LATI_ID, this.fontRenderer, this.width / 2 + 15, 130, 170, 20);
        longitudeField.setMaxStringLength(20);
        latitudeField.setMaxStringLength(20);
        longitudeField.setText("" + this.spawnLong);
        latitudeField.setText("" + this.spawnLat);
        
        this.mapButton = new GuiButton(PICK_ID, this.width/2 - 160, 100, 50, 50, "");
        this.buttonList.add(mapButton);
        this.smap = new GuiSimpleMap(this.width / 2 - 170, 100, 60);
        this.smap.showPointer = true;

        this.buttonList.add(new GuiButton(RAND_ID, this.width/2 - 160, 170, 150, 20, I18n.format("irlwworldgui.randomize_spawn")));

        GuiButton presetButton = new GuiButton(PRESET_ID, this.width/2 + 10, 170, 150, 20, I18n.format("irlwworldgui.preset_spawn")); //TODO Implement preset spawn points
        presetButton.enabled = false;
        this.buttonList.add(presetButton);
        
        this.updateJsonFromValues();
        
    }
    
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
    	this.drawDefaultBackground();
    	super.drawScreen(mouseX, mouseY, partialTicks);
    	this.generatorJson.drawTextBox();
    	this.longitudeField.drawTextBox();
    	this.latitudeField.drawTextBox();
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwworldgui.title", new Object[0]), this.width / 2, 20, -1);
    	this.drawString(this.fontRenderer, I18n.format("irlwworldgui.json"), this.width / 2 - 150, 46, -1);
    	this.drawString(this.fontRenderer, I18n.format("irlwworldgui.spawn_long"), this.width / 2 - 100, 106, -1);
    	this.drawString(this.fontRenderer, I18n.format("irlwworldgui.spawn_lat"), this.width / 2 - 100, 136, -1);
    	long worldSize = 1<<(this.zoomLevel + 8);
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwworldgui.world_size", worldSize), this.width / 2 + 98, 76, 0xFFFFFFFF);
    	this.smap.draw();
    	this.smap.setPointerLongLat(this.spawnLong, this.spawnLat, 0xFFFF0000);
    	if(this.mapButton.isMouseOver()) {
    		this.drawHoveringText(I18n.format("irlwworldgui.spawn_picking"), mouseX, mouseY);
    	}
    }
    
    
    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen(){
    	this.generatorJson.updateCursorCounter(); //Don't know what it really does, but present in existing game guis :(
    	super.updateScreen();
    }
    
    
    /**
     * Called by the game when a button is pressed.
     * @throws IOException 
     */
    protected void actionPerformed(GuiButton button) throws IOException{
    	super.actionPerformed(button);
    	switch(button.id){
    	case DONE_ID:
    		save();
    		Keyboard.enableRepeatEvents(false); //We need to disable that, we activated it when the gui opened
    		this.mc.currentScreen = this.createWorldGui;
    		this.mc.updateDisplay();
    		break;
    	case RAND_ID:
    		Random random = new Random();
    		this.spawnLat = (random.nextDouble() - .5) * 180;
    		this.spawnLong = (random.nextDouble() - .5) * 360;
    		this.latitudeField.setText("" + this.spawnLat);
    		this.longitudeField.setText("" + this.spawnLong);
    		this.updateJsonFromValues();
    		break;
    	case PICK_ID:
    		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenPickLocation(this));
    	}
    }
    
    
    /**
     * Saves the gui's settings to the configuration object and then saves it.
     */
    private void save(){
    	this.createWorldGui.chunkProviderSettingsJson = this.generatorJson.getText();
    }
    
    
    /**
     * Called by the game when the mouse is clicked, has to be passed to the components of the gui to have them react,
     * and to super for automatic button process
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	this.generatorJson.mouseClicked(mouseX, mouseY, mouseButton);
    	this.latitudeField.mouseClicked(mouseX, mouseY, mouseButton);
    	this.longitudeField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    
    /**
     * Called by the game when a key is pressed, the event has to be passed to the guis's components if the focus is on them.
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
    	super.keyTyped(typedChar, keyCode);
    	if(this.generatorJson.isFocused()){
    		String txt = this.generatorJson.getText();
    		this.generatorJson.textboxKeyTyped(typedChar, keyCode);
    		if(!txt.equals(generatorJson.getText())) {
    			this.updateValuesFromJson();
    		}
    	}
    	if(this.longitudeField.isFocused()){
    		String txt = this.longitudeField.getText();
    		this.longitudeField.textboxKeyTyped(typedChar, keyCode);
    		if(!txt.equals(longitudeField.getText())) {
    			try {
    				this.spawnLong = Double.parseDouble(this.longitudeField.getText());
    				this.longitudeField.setTextColor(0xFFFFFF);
    				this.updateJsonFromValues();
    			}catch(NumberFormatException e) {
    				this.longitudeField.setTextColor(0xFF3000);
    			}
    		}
    	}
    	if(this.latitudeField.isFocused()){
    		String txt = this.latitudeField.getText();
    		this.latitudeField.textboxKeyTyped(typedChar, keyCode);
    		if(!txt.equals(latitudeField.getText())) {
    			try {
    				this.spawnLat = Double.parseDouble(this.latitudeField.getText());
    				this.latitudeField.setTextColor(0xFFFFFF);
    				this.updateJsonFromValues();
    			}catch(NumberFormatException e) {
    				this.latitudeField.setTextColor(0xFF3000);
    			}
    		}
    	}
    }

	@Override
	public void setEntryValue(int id, String value) {		
	}
	
	@Override
	public void setEntryValue(int id, float value) {
		switch(id) {
		
		case ZOOM_ID:
			this.zoomLevel = (int) value;
			break;
		}
		
		this.updateJsonFromValues();
	}
	
	@Override
	public void setEntryValue(int id, boolean value) {
	}
	
	public void updateJsonFromValues() {
        generatorJson.setText("{\"zoomlevel\": " + this.zoomLevel + ", \"spawn_long\": " + this.spawnLong + ", \"spawn_lat\": " + this.spawnLat + "}");
	}
	
	public void updateValuesFromJson() {
		
		JsonParser parser = new JsonParser();
		try{
    		JsonObject jsonObject = parser.parse(this.generatorJson.getText()).getAsJsonObject();
    		this.zoomLevel = jsonObject.get("zoomlevel").getAsInt();
    		this.spawnLong = jsonObject.get("spawn_long").getAsDouble();
    		this.spawnLat = jsonObject.get("spawn_lat").getAsDouble();
    	}catch(Exception e){
    		this.generatorJson.setTextColor(0xFF3000);
    		return;
    	}
		
		this.generatorJson.setTextColor(0xFFFFFF);
		
		for(GuiButton comp: this.buttonList) {
			switch(comp.id){
			case ZOOM_ID:
				((GuiSlider) comp).setSliderValue(this.zoomLevel, false);
				break;
			}
			
		}
		
		this.longitudeField.setText("" + this.spawnLong);
		this.latitudeField.setText("" + this.spawnLat);
		
	}
	
}
