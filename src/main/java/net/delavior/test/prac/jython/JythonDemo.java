package net.delavior.test.prac.jython;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.python.util.PythonInterpreter;

public class JythonDemo {
	public void simplePrint(String printMsg) {
		if (StringUtils.isEmpty(printMsg)) {
			return;
		}
		Properties props = new Properties();
		props.put("python.home", "jython-lib");
		props.put("python.console.encoding", "UTF-8");
		props.put("python.security.respectJavaAccessibility", "false");
		props.put("python.import.site", "false");
		Properties sysProps = System.getProperties();
		PythonInterpreter interpreter = null;
		try {
			PythonInterpreter.initialize(props, sysProps, null);
			interpreter = new PythonInterpreter();
			interpreter.exec("print '" + printMsg + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (interpreter != null) {
				interpreter.close();
			}
		}
	}

	public static void main(String[] args) {
		JythonDemo demo = new JythonDemo();
		demo.simplePrint("Hello World");
	}
}
