package de.ovgu.featureide.fm.core.explanations.evaluation.impl.resources;

import java.io.File;
import java.util.Map;

import de.ovgu.featureide.fm.core.explanations.evaluation.impl.TimingTest;

/**
 * A test using a project as input.
 * 
 * @param <R> the type of the results of each step
 * @author Timo G&uuml;nther
 */
public abstract class ProjectTest<R> extends TimingTest<R> {

	/** The default input directory containing the project. */
	private static final File IN_DIR = new File("../../data");

	/** The name of the project. */
	private final String projectName;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param projectName the name of the proejct; not null
	 * @param iterations number of iterations to run
	 */
	protected ProjectTest(String projectName, int iterations) {
		super(iterations);
		this.projectName = projectName;
	}

	/**
	 * Returns the input directory containing the project.
	 * 
	 * @return the input directory; not null
	 */
	protected File getInDir() {
		return IN_DIR;
	}

	/**
	 * Returns the name of the project.
	 * 
	 * @return the project name; not null
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Returns the directory of the project with the set name.
	 * 
	 * @return the project directory; not null
	 */
	protected File getProjectDir() {
		return getProjectDir(projectName);
	}

	/**
	 * Returns the directory of the project with the given name.
	 * 
	 * @param projectName the name of the project; not null
	 * @return the project directory; not null
	 */
	protected File getProjectDir(String projectName) {
		return new File(getInDir(), projectName);
	}

	@Override
	public Map<String, Object> getTestResults() {
		final Map<String, Object> results = super.getTestResults();
		results.put("Project", getProjectName());
		return results;
	}
}
