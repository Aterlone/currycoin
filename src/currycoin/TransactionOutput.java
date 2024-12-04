package currycoin;

import currycoin.script.Script;

import java.nio.ByteBuffer;

public record TransactionOutput(long value, OutputVerification verification) {
	/**
	 * Check if the locking script is valid for this output.
	 */
	public boolean acceptsScript(Script lockingScript) {
		return verification.matches(lockingScript);
	}

	public int byteSize() {
		return Long.BYTES + verification.byteSize();
	}

	public void apply(ByteBuffer buffer) {
		buffer.putLong(value);
		verification.apply(buffer);
	}

	public static TransactionOutput parseFrom(ByteBuffer buffer) {
		long value = buffer.getLong();
		OutputVerification verification = OutputVerification.parseFrom(buffer);
		return new TransactionOutput(value, verification);
	}

	/**
	 * The type of locking script used to claim this output.
	 */
	public sealed interface OutputVerification {
		boolean matches(Script lockingScript);
		int byteSize();
		void apply(ByteBuffer buffer);

		int HASH_OPCODE = 0xffff_ffff;
		static OutputVerification parseFrom(ByteBuffer buffer) {
			int length = buffer.getInt();
			if (length == HASH_OPCODE) {
				return new ScriptHash(Hash.parseFrom(buffer));
			} else {
				ByteBuffer slice = buffer.slice().limit(length);
				buffer.position(buffer.position() + length);
				return new ScriptMatch(Script.parseFrom(slice));
			}
		}
	}

	public record ScriptHash(Hash scriptHash) implements OutputVerification {
		@Override
		public boolean matches(Script lockingScript) {
			return lockingScript.hash().equals(scriptHash);
		}

		@Override
		public int byteSize() {
			return Integer.BYTES + scriptHash.byteSize();
		}

		@Override
		public void apply(ByteBuffer buffer) {
			buffer.putInt(HASH_OPCODE);
			scriptHash.apply(buffer);
		}
	}

	public record ScriptMatch(Script lockingScript) implements OutputVerification {
		@Override
		public boolean matches(Script script) {
			return lockingScript.equals(script);
		}

		@Override
		public int byteSize() {
			return Integer.BYTES + lockingScript.byteSize();
		}

		@Override
		public void apply(ByteBuffer buffer) {
			buffer.putInt(lockingScript().byteSize());
			lockingScript.apply(buffer);
		}
	}
}
