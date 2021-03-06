package upb.ida.intent.executor;

import upb.ida.intent.AnswerHandlingStrategy;
import upb.ida.intent.exception.IntentException;
import upb.ida.intent.model.ChatbotContext;
import upb.ida.intent.model.Question;

import java.util.Arrays;
import java.util.Collections;

public class SunBurstExecutor extends AbstractExecutor implements IntentExecutor {

	public SunBurstExecutor() {
		super(Collections.unmodifiableList(
				Arrays.asList(
						new Question("Which column should be sequenced?", Collections.singletonList("col1"), "Column 1 is set to ${col1}.", null, AnswerHandlingStrategy.ACTIVE_TABLE_COLUMNS, false),
						new Question("Which column values should it be sequenced upon?", Collections.singletonList("col2"), "Column 1 is set to ${col2}.", null, AnswerHandlingStrategy.ACTIVE_TABLE_COLUMNS, false)
				))
		);
	}

	@Override
	public boolean execute(ChatbotContext context) throws IntentException {
		// Placeholder
		return true;
	}
}
