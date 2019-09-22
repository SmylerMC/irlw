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

import org.framagit.smylermc.irlw.client.gui.widget.SimpleMapWidget;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;


//TODO world customization GUI Does not remember settings if closed and reopened

/**
 * This will change a lot in the future, as it is not the current main focus
 * 
 * @author Smyler
 *
 */
public class CustomizeIRLWorldScreen extends Screen{
	
	
	private CreateWorldScreen createWorldGui;
			
	private TextFieldWidget	 generatorJson;
	private TextFieldWidget longitudeField;
	private TextFieldWidget latitudeField;
	
	private double spawnLong = 0;
	private double spawnLat = 0;
	
	private SimpleMapWidget smap;
	private Button mapButton;
	private int zoomLevel;
	

	/**
	 * Constructor
	 * 
	 * @param mc
	 * @param guiCreateWorld
	 */
    public CustomizeIRLWorldScreen(Minecraft mc, CreateWorldScreen guiCreateWorld) {
    	super(new TranslationTextComponent("")); //FIXME 1.14.4 - Customize IRL world screen title
		this.minecraft = mc;
		this.createWorldGui = guiCreateWorld;
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
        
        generatorJson = new TextFieldWidget(this.font, this.width / 2 - 50, 40, 200, 20, "generator parameters");//TODO Localization
        generatorJson.setMaxStringLength(500);
        
        //FIXME 1.14.4 - Customize IRLW World zoom GUI Slider
//        this.buttons.add(new GuiSlider(this, ZOOM_ID, this.width / 2 - 175, 70, "slider", 0f, 20f, (float) this.zoomLevel, new GuiSlider.FormatHelper() {
//
//			@Override
//			public String getText(int id, String name, float value) {
//				return I18n.format("irlwworldgui.zoom_level") + ": " + zoomLevel;
//			}
//			
//		}));
        
        longitudeField = new TextFieldWidget(this.font, this.width / 2 + 15, 100, 170, 20, "longitude"); //TODO Localization
        latitudeField = new TextFieldWidget(this.font, this.width / 2 + 15, 130, 170, 20, "latitude"); //TODO Localization
        longitudeField.setMaxStringLength(20);
        latitudeField.setMaxStringLength(20);
        longitudeField.setText("" + this.spawnLong);
        latitudeField.setText("" + this.spawnLat);
        
        this.mapButton = new Button(this.width/2 - 160, 100, 50, 50, "", this::letUserPickCoordinates);
        this.addButton(mapButton);
        this.smap = new SimpleMapWidget(this.width / 2 - 170, 100, 60);
        this.smap.showPointer = true;
        this.addButton(this.smap);

        this.addButton(new Button(this.width/2 - 160, 170, 150, 20, I18n.format("irlwworldgui.randomize_spawn"), this::populateRandomCoordinates));

        Button presetButton = new Button(this.width/2 + 10, 170, 150, 20, I18n.format("irlwworldgui.preset_spawn"), null); //TODO Implement preset spawn points
        presetButton.active = false; //FIXME 1.14.4 - Check is active is what we where looking for
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
    	this.drawString(this.font, I18n.format("irlwworldgui.json"), this.width / 2 - 150, 46, -1);
    	this.drawString(this.font, I18n.format("irlwworldgui.spawn_long"), this.width / 2 - 100, 106, -1);
    	this.drawString(this.font, I18n.format("irlwworldgui.spawn_lat"), this.width / 2 - 100, 136, -1);
    	long worldSize = 1<<(this.zoomLevel + 8);
    	this.drawCenteredString(this.font, I18n.format("irlwworldgui.world_size", worldSize), this.width / 2 + 98, 76, 0xFFFFFFFF);
    	//this.smap.render(); //FIXME 1.14.4 - Remove if effectively useless
    	this.smap.setPointerLongLat(this.spawnLong, this.spawnLat, 0xFFFF0000);
    	if(this.mapButton.isMouseOver(mouseX, mouseY)) {
    		this.renderTooltip(I18n.format("irlwworldgui.spawn_picking"), mouseX, mouseY);
    	}
    }
    
    /*//TODO Remove if effectively not needed
    /**
     * Called from the main game loop to update the screen.
     *
    public void updateScreen(){
    	this.generatorJson.updateCursorCounter(); //Don't know what it really does, but present in existing game guis :(
    	super.updateScreen();
    }
    */
    
    
    /**
     * Called when the done button is pressed
     * 
     * @param b
     */
    private void done(Button b) {
    	this.save();
		this.minecraft.displayGuiScreen(this.createWorldGui) ;
		//this.mc.updateDisplay(); //FIXME 1.14.4 - Why was this here ?
    } //TODO
    
    
    
    /**
     * Called when the random spawn point button is pressed
     * 
     * @param b
     */
    private void populateRandomCoordinates(Button b) {
		Random random = new Random();
		this.spawnLat = (random.nextDouble() - .5) * 180;
		this.spawnLong = (random.nextDouble() - .5) * 360;
		this.latitudeField.setText("" + this.spawnLat);
		this.longitudeField.setText("" + this.spawnLong);
		this.updateJsonFromValues();
    }
    
    
    
    /**
     * Called when the minimap is pressed to let user pick a position on a dynmap
     * 
     * @param b
     */
    private void letUserPickCoordinates(Button b) {
    	Minecraft.getInstance().displayGuiScreen(new PickLocationScreen(this));
    }
    
    
    
    /**
     * Saves the gui's settings to the configuration object and then saves it.
     */
    private void save(){
    	//FIXME 1.14.4 - generator's json has changed, figure out how it works and implement it
    	//this.createWorldGui.chunkProviderSettingsJson = this.generatorJson.getText(); 
    }
    
    
    /**
     * Called by the game when the mouse is clicked, has to be passed to the components of the gui to have them react,
     * and to super for automatic button process
     */
    @Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
    	//FIXME 1.14.4 - Test if everything works properly
    	this.generatorJson.mouseClicked(mouseX, mouseY, mouseButton);
    	this.latitudeField.mouseClicked(mouseX, mouseY, mouseButton);
    	this.longitudeField.mouseClicked(mouseX, mouseY, mouseButton);
    	return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

//TODO Make sure this is effectively useless
//
//    /**
//     * Called by the game when a key is pressed, the event has to be passed to the guis's components if the focus is on them.
//     */
//    @Override
//	public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_){
//    	if(this.generatorJson.isFocused()){
//    		String txt = this.generatorJson.getText();
//    		this.generatorJson.textboxKeyTyped(typedChar, keyCode);
//    		if(!txt.equals(generatorJson.getText())) {
//    			this.updateValuesFromJson();
//    		}
//    	}
//    	if(this.longitudeField.isFocused()){
//    		String txt = this.longitudeField.getText();
//    		this.longitudeField.textboxKeyTyped(typedChar, keyCode);
//    		if(!txt.equals(longitudeField.getText())) {
//    			try {
//    				this.spawnLong = Double.parseDouble(this.longitudeField.getText());
//    				this.longitudeField.setTextColor(0xFFFFFF);
//    				this.updateJsonFromValues();
//    			}catch(NumberFormatException e) {
//    				this.longitudeField.setTextColor(0xFF3000);
//    			}
//    		}
//    	}
//    	if(this.latitudeField.isFocused()){
//    		String txt = this.latitudeField.getText();
//    		this.latitudeField.textboxKeyTyped(typedChar, keyCode);
//    		if(!txt.equals(latitudeField.getText())) {
//    			try {
//    				this.spawnLat = Double.parseDouble(this.latitudeField.getText());
//    				this.latitudeField.setTextColor(0xFFFFFF);
//    				this.updateJsonFromValues();
//    			}catch(NumberFormatException e) {
//    				this.latitudeField.setTextColor(0xFF3000);
//    			}
//    		}
//    	}
//    	super.keyPressed(typedChar, keyCode);
//    }
    

//FIXME 1.14.4 Customize world zoom slider
//	@Override
//	public void setEntryValue(int id, float value) {
//		switch(id) {
//		
//		case ZOOM_ID:
//			this.zoomLevel = (int) value;
//			break;
//		}
//		
//		this.updateJsonFromValues();
//	}
	
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
		
		//this.guiSlider.setValue(); //FIXME 1.14.4 - World zoom slider Slider
		
		this.longitudeField.setText("" + this.spawnLong);
		this.latitudeField.setText("" + this.spawnLat);
		
	}
	
}
