package handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter{

	public boolean keyDown(int k){
		if (k == Keys.X) {
			MyInput.setKey(MyInput.BUTTON1, true);
		}
		if (k == Keys.Z) {
			MyInput.setKey(MyInput.BUTTON2, true);
		}
//		if (k == Keys.S) {
//			MyInput.setKey(MyInput.BUTTON3, true);
//		}
//		if (k == Keys.D) {
//			MyInput.setKey(MyInput.BUTTON4, true);
//		}
		return true;
	}
	
	public boolean keyUp(int k){
		if (k == Keys.X) {
			MyInput.setKey(MyInput.BUTTON1, false);
		}
		if (k == Keys.Z) {
			MyInput.setKey(MyInput.BUTTON2, false);
		}
//		if (k == Keys.S) {
//			MyInput.setKey(MyInput.BUTTON3, false);
//		}
//		if (k == Keys.D) {
//			MyInput.setKey(MyInput.BUTTON4, false);
//		}
		return true;
	}
	
}
