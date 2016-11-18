package ch.ffhs.hdo.client.ui.base.viewhandler;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ffhs.hdo.client.ui.base.ParamChecker;

public class ViewHandlerImpl implements ViewHandler {

	static Logger LOGGER = LogManager.getLogger(ViewHandlerImpl.class);

	private final Map<Class<? extends ViewOperation>, Executable[]> opMap;

	public ViewHandlerImpl() {
		opMap = new HashMap<Class<? extends ViewOperation>, Executable[]>();

	}

	public void addOperation(Class<? extends ViewOperation> viewOperation, Executable... executables) {
		opMap.put(viewOperation, executables);

	}

	public void performOperation(Class<? extends ViewOperation> operation) {
		ParamChecker.notNull(operation, "operation");
		checkPreconditions(operation);

		executeOperation(operation, null);
	}

	public void performOperationWithArgs(Class<? extends ViewOperation> operation, Object arg) {
		checkPreconditions(operation);
		executeOperation(operation, arg);
	}

	private void executeOperation(Class<? extends ViewOperation> operation, Object arg) {
		try {
			LOGGER.debug("start Execution");
			performOperation(operation, arg);
		} finally {
			LOGGER.debug("end Execution");
		}
	}

	private void performOperation(Class<? extends ViewOperation> operation, Object arg) {
		Executable[] executable = opMap.get(operation);

		for (Executable exc : executable) {
			exc.execute(arg);
		}
	}

	private void checkPreconditions(Class<? extends ViewOperation> operation) {
		throwExceptionIfOperationIsUnknown(operation);
	}

	private void throwExceptionIfOperationIsUnknown(Class<? extends ViewOperation> operation) {
		if (!opMap.containsKey(operation)) {
			throw new UnsupportedOperationException(operation.getName());
		}
	}

	public void destroy() {
		opMap.clear();
	}

}