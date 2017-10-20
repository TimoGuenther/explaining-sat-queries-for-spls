package de.ovgu.featureide.fm.core.explanations.evaluation.impl.resources;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.io.manager.ConfigurationManager;

/**
 * A test involving a {@link Configuration configuration}.
 * 
 * @param <R> the type of the results of each step
 * @author Timo G&uuml;nther
 */
public abstract class ConfigurationTest<R> extends FeatureModelTest<R> {

	/** The name of the configuration. */
	private final String configName;

	/** The loaded configuration. */
	private Configuration config;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param projectName the name of the project; not null
	 * @param fmName the name of the feature model; not null
	 * @param configName the name of the configuration; not null
	 * @param iterations number of iterations to run
	 */
	protected ConfigurationTest(String projectName, String fmName, String configName, int iterations) {
		super(projectName, fmName, iterations);
		this.configName = configName;
	}

	/**
	 * Returns the name of the configuration.
	 * 
	 * @return the configuration name
	 */
	public String getConfigurationName() {
		return configName;
	}

	/**
	 * Returns the configuration.
	 * The configuration is only set during test execution.
	 * 
	 * @return the configuration
	 */
	protected Configuration getConfiguration() {
		return config;
	}

	@Override
	protected void runBeforeTest() {
		super.runBeforeTest();
		final File configDir = new File(getProjectDir(), "configs");
		final File configFile = new File(configDir, getConfigurationName() + ".config");
		config = new Configuration(getFeatureModel());
		config = ConfigurationManager.load(Paths.get(configFile.getPath()), config).getObject();
	}

	@Override
	protected void runAfterTest() {
		super.runAfterTest();
		config = null;
	}

	@Override
	public Map<String, Object> getTestResults() {
		final Map<String, Object> results = super.getTestResults();
		results.put("Config", getConfigurationName());
		return results;
	}
}
