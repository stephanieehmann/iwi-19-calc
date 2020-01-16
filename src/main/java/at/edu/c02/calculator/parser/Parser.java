package at.edu.c02.calculator.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

import at.edu.c02.calculator.Calculator;
import at.edu.c02.calculator.CalculatorException;
import at.edu.c02.calculator.Calculator.Operation;

public class Parser {

	private final Calculator calc;

	public Parser(Calculator cal) {
		if (cal == null)
			throw new IllegalArgumentException("Calculator not set");
		calc = cal;
	}

	public double parse(File calculation) throws FileNotFoundException,
			XMLStreamException, CalculatorException {

		double result = 0;
		XMLEventReader r = createXmlEventReader(calculation);

		while (r.hasNext()) {
			XMLEvent e = r.nextEvent();
			Attribute attribute = e.asStartElement().getAttributeByName(
					new QName("value"));
			String value = attribute != null ? attribute.getValue() : "";
			if ("push".equals(e.asStartElement().getName().getLocalPart())) {
				if ("Result".equalsIgnoreCase(value)) {
					calc.push(result);
				} else {
					calc.push(Double.parseDouble(value));
				}
			} else if ("pop"
					.equals(e.asStartElement().getName().getLocalPart())) {
				calc.pop();
			} else if ("operation".equals(e.asStartElement().getName()
					.getLocalPart())) {
				result = calc.perform(readOperation(value));
			}
		}

		return result;
	}

	private XMLEventReader createXmlEventReader(File calculation)
			throws FactoryConfigurationError, FileNotFoundException,
			XMLStreamException {
		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		FileReader fr = new FileReader(calculation);
		XMLEventReader xmler = xmlif.createXMLEventReader(fr);
		EventFilter filter = event -> event.isStartElement();

		XMLEventReader r = xmlif.createFilteredReader(xmler, filter);
		return r;
	}

	private Operation readOperation(String value) throws CalculatorException {

		switch (value) {
			case "*":
				return Operation.mul;
			case "+":
				return Operation.add;
			case "/":
				return Operation.div;
			case "-":
				return Operation.sub;
		}
		
		throw new CalculatorException("Unsuppoted Operation");
	}
}
