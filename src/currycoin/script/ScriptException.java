package currycoin.script;

public abstract sealed class ScriptException extends RuntimeException {
	public ScriptException(String message) {
		super(message);
	}

	public ScriptException(Throwable cause) {
		super(cause);
	}

	public ScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	public static final class InvalidScriptException extends ScriptException {
		public InvalidScriptException(String message) {
			super(message);
		}

		public InvalidScriptException(Throwable cause) {
			super(cause);
		}

		public InvalidScriptException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	public static final class StackUnderflowException extends ScriptException {
		public StackUnderflowException(String message) {
			super(message);
		}

		public StackUnderflowException(Throwable cause) {
			super(cause);
		}

		public StackUnderflowException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	public static final class VerificationException extends ScriptException {
		public VerificationException(String message) {
			super(message);
		}
	}
}
