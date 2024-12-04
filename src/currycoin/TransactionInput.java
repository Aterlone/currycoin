package currycoin;

import currycoin.script.Script;
import currycoin.script.ScriptException;
import currycoin.script.ScriptStack;

public record TransactionInput(Hash prevTransaction, int index, Script lockingScript, Script unlockingScript) {
	/**
	 * Checks whether the provided unlocking script can unlock the provided locking script.
	 * Does not check if the provided locking script is correct the output it is trying to unlock.
	 */
	public boolean unlocks(Hash dataToSign) {
		ScriptStack stack = new ScriptStack(dataToSign);
		try {
			lockingScript.execute(stack);
			unlockingScript.execute(stack);
			return true; // ran without failing
		} catch (ScriptException e) {
			return false;
		}
	}
}
