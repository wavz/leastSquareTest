package ols;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import linear_systems_solver.LinearSystemSolver;
import linear_systems_solver.Solver;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class OlsRegression {
	private String parametricFit;
	private String[] params;
	private double[] xSet;
	private double[] ySet;
	private static Process pythonProcces;
	
	public OlsRegression(String parametricFit, String[] params, double[] xSet, double[] ySet) {
		if (parametricFit == null || params == null || xSet == null || ySet == null)
			throw new NullPointerException();
		if (xSet.length != ySet.length)
			throw new IllegalArgumentException();
		this.xSet = xSet;
		this.ySet = ySet;
		this.params = params;
		this.parametricFit = parametricFit;
	}
	
	public Expression getRegressionFit() {
		double[][] A = generateDataSetMatrix();
		double[][] At = MatrixOp.transposeMatrix(A);
		double[][] AtA = MatrixOp.multipliMatrices(At, A);
		double[] AtY = MatrixOp.multipliMatrices(At, ySet);
		LinearSystemSolver solver = new Solver(AtA, AtY);
		double[] x = solver.solveSystem();
		Expression e = new ExpressionBuilder(parametricFit).variables(params).variable("x").build();
		for (int i = 0; i < params.length; i++)
			e.setVariable(params[i], x[i]);
		return e;
	}

	private double[][] generateDataSetMatrix() {
		double[][] A = new double[xSet.length][params.length];
		String[] fi = parametricFit.split("\\+");
		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[0].length; j++) {
				ExpressionBuilder expBuilder = new ExpressionBuilder(fi[j]).variables(params).variable("x");
				Expression e = expBuilder.build().setVariable(params[j],  1).setVariable("x", xSet[i]);
				A[i][j] = e.evaluate();
			}
		return A;
	}
	
	public static void main(String[] args) throws IOException {
//		double[] x = new double[] {- Math.PI/2, 0, Math.PI/2, Math.PI} ;
//		double[] y = new double[] {1, -1, 0, 0};
//		String fn = "ax+b";
//		OlsRegression r = new OlsRegression(fn, new String[] {"a", "b"}, x, y);
//		Expression e = r.getRegressionFit();
//
//		StdDraw.setYscale(-5, 5);
//		StdDraw.setXscale(-5, 5);
//
//		for (double i = -20; i < 20; i = i + 0.05)
//			StdDraw.line(i, e.setVariable("x", i).evaluate(), i + 0.05, e.setVariable("x", i + 0.05).evaluate());
//
//		StdDraw.setPenColor(Color.RED);
//		StdDraw.setPenRadius(0.01);
//		for (int i = 0; i < x.length; i++)
//			StdDraw.point(x[i], y[i]);

		String pythonScriptPath = "C:\\Users\\איתי\\PycharmProjects\\untitled\\spline.py";
		String[] cmd = new String[2];

		cmd[0] = "python";
		cmd[1] = pythonScriptPath;

		double[] arr1 = {1,2,3};
		double[] arr2 = {4,5,3};

//		cmd[2] = Arrays.toString(arr1);
//		cmd[3] = Arrays.toString(arr2);

		Runtime rt = Runtime.getRuntime();
		try {
			Process pr = rt.exec("python C:\\Users\\איתי\\PycharmProjects\\untitled\\Main.py");
			pythonProcces = pr;
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader bfr = new BufferedReader(new InputStreamReader(pythonProcces.getInputStream()));
		String line = "";
		while((line = bfr.readLine()) != null) {
// display each output line form python script
			System.out.println(line);
		}
	}
}
