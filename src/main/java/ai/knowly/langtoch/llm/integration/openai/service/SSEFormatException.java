package ai.knowly.langtoch.llm.integration.openai.service;

/**
 * Exception indicating a SSE format error
 */
public class SSEFormatException extends Throwable{
	public SSEFormatException(String msg){
		super(msg);
	}
}