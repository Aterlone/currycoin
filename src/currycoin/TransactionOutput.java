package currycoin;

import currycoin.script.Script;

import java.util.Objects;
import java.util.Optional;

public record TransactionOutput(long value, boolean containsScript, Script lockingScript, Hash scriptHash) {
	public TransactionOutput {
		// either provides a locking script, or only a hash
		if (containsScript) {
			Objects.requireNonNull(lockingScript);
			scriptHash = lockingScript.hash();
		} else {
			Objects.requireNonNull(scriptHash);
			lockingScript = null;
		}
	}

	public Optional<Script> getLockingScript() {
		return Optional.ofNullable(lockingScript);
	}
}
