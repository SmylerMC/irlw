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

import java.io.IOException;
import java.util.ArrayList;

import org.framagit.smylermc.irlw.IRLW;
import org.framagit.smylermc.irlw.IRLWConfiguration;
import org.framagit.smylermc.irlw.maps.utils.MapboxUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Smyler
 *
 */
public class GuiInvalidTokenPrompt extends GuiScreen implements Runnable{

	
	/**
	 * Set to true once the player has answered to the token prompt.
	 */
	public boolean isDone = false;
	private GuiScreen parent;
	private GuiTextField tokenField;
	private TokenChecker checker;
	private GuiButton ignoreButton;
	private ArrayList<String> ignoreTxt = new ArrayList<String>();
	
	
	public GuiInvalidTokenPrompt(GuiScreen parent) {
		this.parent = parent;
	}

	
	public GuiInvalidTokenPrompt() {
		this(Minecraft.getMinecraft().currentScreen);
	}
	
	
	public boolean isDone() {
		return this.isDone;
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
        this.buttonList.add(new GuiButton(0, this.width / 2 - 175, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.ignoreButton = new GuiButton(1, this.width / 2 + 25, this.height - 28, 150, 20, I18n.format("irlwtokengui.ignore", new Object[0]));
        this.buttonList.add(ignoreButton);
        
        //Mapbox token text field
        tokenField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 50, 60, 200, 20);
        tokenField.setMaxStringLength(89);
        this.tokenField.setText(IRLWConfiguration.mapboxToken);
        
        this.ignoreTxt.add(I18n.format("irlwtokengui.ignorehover", new Object[0]));
        if(Minecraft.getMinecraft().isIntegratedServerRunning())
        	this.ignoreTxt.add(I18n.format("irlwtokengui.ignorehover.running", new Object[0]));
        
        this.checker = new TokenChecker(IRLWConfiguration.mapboxToken);
        
    }
    
    /**
     * Draws the screen and all the components in it.
     */
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
    	this.drawDefaultBackground();
    	this.tokenField.drawTextBox();
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwtokengui.title", new Object[0]), this.width / 2, 20, -1);
    	this.drawString(this.fontRenderer, I18n.format("irlwtokengui.token"), this.width / 2 - 150, 66, -1);
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwtokengui.text1", new Object[0]), this.width/2, 120, -1);
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwtokengui.text2", new Object[0]), this.width/2, 140, -1);
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwtokengui.text3", new Object[0]), this.width/2, 160, -1); //TODO This is a link, make it work like so.
    	this.drawCenteredString(this.fontRenderer, I18n.format("irlwtokengui.text4", new Object[0]), this.width/2, 180, -1);
    	this.drawCenteredString(this.fontRenderer, checker.getState(), this.width/2, 100, -1);
//    	if(!this.safeToShutdown)
//    		this.drawCenteredString(this.fontRendererObj, TextFormatting.DARK_RED + "/!\\ " + I18n.format("irlwtokengui.notsafe", new Object[0]) + " /!\\", this.width/2, 220, -1);
    	super.drawScreen(mouseX, mouseY, partialTicks);
    	if(this.ignoreButton.x <= mouseX &&
    			this.ignoreButton.x + this.ignoreButton.getButtonWidth() >= mouseX &&
    			this.ignoreButton.y <= mouseY &&
    			this.ignoreButton.y + this.ignoreButton.height >= mouseY) {
    		this.drawHoveringText(ignoreTxt, mouseX, mouseY);
    	}
    }
    
    
    /**
     * Called from the main game loop to update the screen.
     */
	@Override
    public void updateScreen(){
    	this.tokenField.updateCursorCounter(); //Don't know what it really does, but present in existing game guis :(
    	if(this.checker.isValid()) {
    		this.save();
    		Minecraft.getMinecraft().displayGuiScreen(this.parent);
    		//this.parent.initGui();
    	}
    	super.updateScreen();
    }
    
    /**
     * Called by the game when a button is pressed.
     * @throws IOException 
     */
	@Override
    protected void actionPerformed(GuiButton button) throws IOException{
    	switch(button.id){
    	case 0:
    		if(!this.checker.isValid() && !this.checker.state.equals("checking")) {
    			Thread t = new Thread(checker);
    			t.setName("Token Checker");
    			t.start();
    		}
    		break;
    	case 1:
    		this.isDone = true;
    		IRLW.cacheManager.clearQueue();
    		IRLWConfiguration.ignoreInvalidTokens = true;
    		IRLW.proxy.stopServer();
    		Minecraft.getMinecraft().displayGuiScreen(this.parent);
    	}
    	super.actionPerformed(button);
    }
    
    
    /**
     * Saves the gui's settings to the configuration object and then saves it.
     */
    private void save(){
    	IRLWConfiguration.mapboxToken = this.tokenField.getText();
    	IRLWConfiguration.sync();
    	this.isDone = true;
    	IRLW.logger.info("Saved new token");
    }
    
    
    /**
     * Called by the game when the mouse is clicked, has to be passed to the components of the gui to have them react,
     * and to super for automatic button process
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    
    /**
     * Called by the game when a key is pressed, the event has to be passed to the guis's components if the focus is on them.
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
    	super.keyTyped(typedChar, keyCode);
    	if(this.tokenField.isFocused()){
    		this.tokenField.textboxKeyTyped(typedChar, keyCode);
    		this.checker.setToken(this.tokenField.getText());
    	}
    }
    
    
    @Override
    public boolean doesGuiPauseGame() {
    	return true;
    }
    
    
    private class TokenChecker implements Runnable{
    	
    	private String state = "waiting";
    	private String lastTokenChecked = "";
    	private String currentToken;


		public TokenChecker(String token) {
    		this.currentToken = token;
    	}


    	public void setToken(String token) {
    		this.currentToken = token;
    	}
    	
    	
    	public String getState() {
    		if(!this.state.equals("checking")  && !this.currentToken.equals(this.lastTokenChecked)) {
    			this.state = "waiting";
    		}
        	if(this.state.equals("checking")) {
        		return TextFormatting.YELLOW + I18n.format("irlwtokengui.cheking");
        	}else if(this.state.equals("invalid")){
        		return TextFormatting.RED + I18n.format("irlwtokengui.invalid");
        	}else if(this.state.equals("valid")){
        		return TextFormatting.GREEN + I18n.format("irlwtokengui.valid");
        	}
        	return "";
        }
    	
    	
    	public boolean isValid() {
    		return this.state.equals("valid");
    	}
    	
    	
		@Override
		public void run() {
			if(this.state.equals("checking")) {
				return;
			}
			IRLW.logger.info("Checking token...");
			this.state = "checking";
			boolean valid = MapboxUtils.checkToken(this.currentToken);
			this.lastTokenChecked = this.currentToken;
			IRLW.logger.info("New token is " + (valid? "Valid": "Invalid"));
			this.state = valid? "valid": "invalid";
		}
    	
    }


	@Override
	public void run() {
		Minecraft.getMinecraft().displayGuiScreen(this);
	}
}
