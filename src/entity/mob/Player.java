package entity.mob;

import entity.projectile.Projectile;
import entity.projectile.WizardProjectile;
import myGame.Game;
import myGame.graphics.AnimatedSprite;
import myGame.graphics.Screen;
import myGame.graphics.Sprite;
import myGame.graphics.SpriteSheet;
import myGame.graphics.ui.UILabel;
import myGame.graphics.ui.UIManager;
import myGame.graphics.ui.UIPanel;
import myGame.input.Keyboard;
import myGame.input.Mouse;
import util.Vector2i;

public class Player extends Mob {

	private Keyboard input;
	private Sprite sprite;
	// private int anim = 0;
	// private boolean walking = false;
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);

	private AnimatedSprite animSprite = down; // default sprite

	private int fireRate = 0;

	private UIManager ui;

	public Player(Keyboard input) {
		this.input = input;
		// sprite = Sprite.player_forward;
	}

	public Player(int x, int y, Keyboard input) {
		this.x = x;
		this.y = y;
		this.input = input;
		// sprite = Sprite.player_forward;
		fireRate = WizardProjectile.FIRE_RATE;
		// create the ui for the game
		ui = Game.getUIManager();
		UIPanel panel = new UIPanel(new Vector2i((300 - 80) * 3, 0), new Vector2i(80 * 3, 168 * 3));
		ui.addPanel(panel);
		panel.addComponent(new UILabel(new Vector2i(10, 50), "roi").setColor(0));
	}

	@Override
	public void update() {
		if (walking)
			animSprite.update(); // player walk
		else
			animSprite.setFrame(0); // player stop and reset to default
		if (fireRate > 0) // count for shooting
			fireRate--;
		double xa = 0, ya = 0;
		double speed = 1.4;
		if (input.up) {
			animSprite = up;
			ya -= speed;
		} else if (input.down) {
			animSprite = down;
			ya += speed;
		}
		if (input.left) {
			animSprite = left;
			xa -= speed;
		} else if (input.right) {
			animSprite = right;
			xa += speed;
		}
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else {
			walking = false;
		}
		clear();
		updateShooting();
	}

	private void clear() {
		for (int i = 0; i < level.getProjectiles().size(); i++) {
			Projectile p = level.getProjectiles().get(i);
			if (p.isRemoved())
				level.getProjectiles().remove(i);
		}

	}

	private void updateShooting() {
		if (Mouse.getButton() == 1 && fireRate <= 0) {
			double dx = Mouse.getX() - (Game.getWindowWidth() / 2);
			double dy = Mouse.getY() - (Game.getWindowHeight() / 2);
			double dir = Math.atan2(dy, dx);
			shoot(x, y, dir);
			fireRate = WizardProjectile.FIRE_RATE;
		}
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		// put player on the screen
		screen.renderMob((int) (x - 16), (int) (y - 16), sprite);
	}

}
