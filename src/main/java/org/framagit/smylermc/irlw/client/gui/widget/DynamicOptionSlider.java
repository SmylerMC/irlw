package org.framagit.smylermc.irlw.client.gui.widget;

import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.settings.SliderPercentageOption;

/**
 * A version of OptionSlider with an updateValue method
 * This allows other sources to modify the represented value without breaking the slider
 * Couldn't just override OptionSlider as OptionSlider#options is private
 * 
 * @author Smyler
 *
 */
public class DynamicOptionSlider extends AbstractSlider{

	   private final SliderPercentageOption option;

	   public DynamicOptionSlider(GameSettings settings, int xIn, int yIn, int widthIn, int heightIn, SliderPercentageOption option) {
	      super(settings, xIn, yIn, widthIn, heightIn, option.func_216726_a(option.get(settings)));
	      this.option = option;
	      this.updateMessage();
	   }

	   protected void applyValue() {
	      this.option.set(this.options, this.option.func_216725_b(this.value));
	      this.options.saveOptions();
	   }

	   protected void updateMessage() {
	      this.setMessage(this.option.func_216730_c(this.options));
	   }
	
	   public void updateValue() {
		   this.value = this.option.func_216726_a(this.option.get(this.options));
		   this.updateMessage();
	   }

}
