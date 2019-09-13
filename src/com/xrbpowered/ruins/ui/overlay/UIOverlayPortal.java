package com.xrbpowered.ruins.ui.overlay;

import java.awt.Color;

import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.ruins.Ruins;
import com.xrbpowered.ruins.entity.DamageSource;
import com.xrbpowered.ruins.entity.player.PlayerEntity;
import com.xrbpowered.ruins.ui.UIHud;
import com.xrbpowered.ruins.world.World;
import com.xrbpowered.ruins.world.item.EmptyFlask;
import com.xrbpowered.ruins.world.item.Item;
import com.xrbpowered.zoomui.UIContainer;

public class UIOverlayPortal extends UIOverlay {

	public static final int memoriesAmuletReq = 5;
	public static final int sacrificeHealth = 90;
	
	private final UIChoicePane wealthButton;
	private final UIChoicePane memoriesButton;
	private final UIChoicePane bodyButton;
	private final UIChoicePane closeButton;
	
	public final UITextOnly title;
	public final UIPane question;
	public final UIText questionText;

	public World world;
	public PlayerEntity player;
	
	public UIOverlayPortal(UIContainer parent) {
		super(parent);
		dismissOnRightClick = true;
		
		wealthButton = new UIChoicePane(this, "My wealth is my burden", "Lose all gold, treasure, and keys!", "Requires "+Item.treasure.name) {
			@Override
			public void onAction() {
				dismiss();
				player.coins = 0;
				player.inventory.removeAll(Item.treasure);
				player.inventory.removeAll(Item.key);
				player.inventory.removeAll(Item.royalKey);
				next();
			}
		};
		memoriesButton = new UIChoicePane(this, "My memories are my burden", "Lose all amulets!", "Requires "+Item.amuletOfEscape.countString(memoriesAmuletReq)) {
			@Override
			public void onAction() {
				dismiss();
				player.inventory.removeAll(Item.amuletOfEscape);
				player.inventory.removeAll(Item.amuletOfRadiance);
				next();
			}
		};
		bodyButton = new UIChoicePane(this, "My body is my burden", String.format("Lose %d Health, all water and herbs!", sacrificeHealth), "Requires full health") {
			@Override
			public void onAction() {
				dismiss();
				player.applyDamage(sacrificeHealth, false, DamageSource.portal);
				player.hydration = 0f;
				EmptyFlask.empty(player.inventory);
				player.inventory.removeAll(Item.healingHerbs);
				if(player.alive)
					next();
			}
		};
		closeButton = new UIChoicePane(this, "I am not ready", "Return to the current level...", "") {
			@Override
			public void onAction() {
				dismiss();
			}
		};

		title = new UITextOnly(this, "Exit Portal").setFont(UIHud.fontBold).setColor(Color.WHITE);
		title.setSize(200, 50);
		
		question = new UISolidPane.Clear(this);
		question.setSize(wealthButton.getWidth(), wealthButton.getHeight());
		questionText = new UIText(question);
		questionText.setHtml("<p>To go further you must leave something behind.<br>What will you sacrifice?</p>");
	}
	
	@Override
	public void layout() {
		closeButton.setLocation(getWidth()/2f-closeButton.getWidth()/2f, getHeight()-100);
		title.setLocation(getWidth()/2f-title.getWidth()/2f, 100);
		float h = wealthButton.getHeight() + 20;
		float x = getWidth()/2f-wealthButton.getWidth()/2f;
		float y = getHeight()/2f - (5*h - 20)/2f; 

		question.setLocation(x, y);
		y += h;
		wealthButton.setLocation(x, y);
		y += h;
		memoriesButton.setLocation(x, y);
		y += h;
		bodyButton.setLocation(x, y);
		y += h;
		closeButton.setLocation(x, y);

		super.layout();
	}

	public void next() {
		World world = Ruins.world;
		if(world.level>=World.maxLevel) {
			Ruins.ruins.enableObserver(true);
			Ruins.flash.blackOut();
			Ruins.overlayVictory.show();
		}
		else {
			Ruins.ruins.restart(world.difficulty, world.level+1, world.player, false);
		}
	}

	public void show(World world) {
		this.world = world;
		this.player = world.player;
		
		wealthButton.setEnabled(player.inventory.has(Item.treasure));
		memoriesButton.setEnabled(player.inventory.count(Item.amuletOfEscape)>=memoriesAmuletReq);
		bodyButton.setEnabled(player.health>=PlayerEntity.baseHealth);
		
		Ruins.ruins.setOverlay(Ruins.overlayPortal);
	}

}
