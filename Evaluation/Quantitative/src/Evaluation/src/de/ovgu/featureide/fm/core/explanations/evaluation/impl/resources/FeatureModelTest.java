package de.ovgu.featureide.fm.core.explanations.evaluation.impl.resources;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

/**
 * A test involving a {@link IFeatureModel feature model}.
 * 
 * @param <R> the type of the results of each step
 * @author Timo G&uuml;nther
 */
public abstract class FeatureModelTest<R> extends ProjectTest<R> {

	/** The name of the feature model. */
	private final String fmName;

	/** The loaded feature model. */
	private IFeatureModel fm;
	/** The number of features in the feature model. */
	private int fs;
	/** The number of constraints in the feature model. */
	private int cs;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param projectName the name of the project; not null
	 * @param fmName the name of the feature model; not null
	 * @param iterations number of iterations to run
	 */
	protected FeatureModelTest(String projectName, String fmName, int iterations) {
		super(projectName, iterations);
		this.fmName = fmName;
	}

	/**
	 * Returns the name of the feature model.
	 * 
	 * @return the feature model name; not null
	 */
	public String getFeatureModelName() {
		return fmName;
	}

	/**
	 * Returns the feature model.
	 * The feature model is only set during test execution.
	 * 
	 * @return the feature model
	 */
	protected IFeatureModel getFeatureModel() {
		return fm;
	}

	/**
	 * Loads the feature model from the project with the given name.
	 * 
	 * @param projectName the name of the project
	 * @return the project's feature model
	 */
	protected IFeatureModel getFeatureModel(String projectName) {
		final File file = new File(getProjectDir(projectName), getFeatureModelName() + ".xml");
		return FeatureModelManager.load(Paths.get(file.getPath())).getObject();
	}

	@Override
	protected void runBeforeTest() {
		super.runBeforeTest();
		fm = getFeatureModel(getProjectName());
		fs = fm.getNumberOfFeatures();
		cs = fm.getConstraintCount();
	}

	@Override
	protected void runAfterTest() {
		super.runAfterTest();
		fm = null;
	}

	@Override
	public Map<String, Object> getTestResults() {
		final Map<String, Object> results = super.getTestResults();
		results.put("FM", getFeatureModelName());
		results.put("Fs", fs);
		results.put("Cs", cs);
		return results;
	}
}
