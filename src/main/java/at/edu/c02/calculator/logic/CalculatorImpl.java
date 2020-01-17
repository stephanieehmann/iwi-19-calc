package at.edu.c02.calculator.logic;

import java.util.Stack;

import at.edu.c02.calculator.Calculator;
import at.edu.c02.calculator.CalculatorException;


public class CalculatorImpl implements Calculator {

	private final Stack<Double> stack = new Stack<>();

	@Override
	public double perform(Operation op) throws CalculatorException {

		double b = pop();
		double a = pop();

		switch (op) {
		case add:
			return a + b;
			case sub:
				return a - b;
			case div:
				double c = a / b;
				if (Double.isInfinite(c))
					throw new CalculatorException("Division by zero");
				return c;
			case mul:
				return a * b;
			case mod:
				return a % b;
		}
		return 0;
	}

	@Override
	public double pop() throws CalculatorException {
		if (stack.isEmpty())
			throw new CalculatorException();
		return stack.pop();
	}

	@Override
	public void push(double v) {
		stack.push(v);
	}

	@Override
	public void clear() {
		stack.clear();
	}

}
