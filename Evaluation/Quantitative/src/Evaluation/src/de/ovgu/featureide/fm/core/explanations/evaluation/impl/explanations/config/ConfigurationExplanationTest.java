package de.ovgu.featureide.fm.core.explanations.evaluation.impl.explanations.config;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.ovgu.featureide.fm.core.configuration.SelectableFeature;
import de.ovgu.featureide.fm.core.explanations.Explanation;
import de.ovgu.featureide.fm.core.explanations.config.AutomaticSelectionExplanationCreator;
import de.ovgu.featureide.fm.core.explanations.config.ConfigurationExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.config.impl.composite.CompositeConfigurationExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.config.impl.ltms.LtmsConfigurationExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.config.impl.mus.MusConfigurationExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.evaluation.Test;
import de.ovgu.featureide.fm.core.explanations.evaluation.TestRunner;
import de.ovgu.featureide.fm.core.explanations.evaluation.impl.resources.ConfigurationTest;

public class ConfigurationExplanationTest extends ConfigurationTest<Explanation<?>> {

	/** The LTMS factory. */
	private static final ConfigurationExplanationCreatorFactory LTMS = new LtmsConfigurationExplanationCreatorFactory();
	/** The MUS factory. */
	private static final ConfigurationExplanationCreatorFactory MUS = new MusConfigurationExplanationCreatorFactory();
	/** The composite factory. */
	private static final ConfigurationExplanationCreatorFactory COMPOSITE = new CompositeConfigurationExplanationCreatorFactory();

	/** The factory used to construct explanation creators. */
	private final ConfigurationExplanationCreatorFactory factory;

	/** The explanation creator for automatic selections. */
	private AutomaticSelectionExplanationCreator asc;

	/** Iterates over feature selections. */
	private Iterator<SelectableFeature> si;

	/** The number of automatic selections found. */
	private int ass;
	/** The number of automatic selections explained. */
	private int asExpl;

	/** The total number of reasons of all found explanations. */
	private int reasons;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param projectName the name of the project; not null
	 * @param fmName the name of the feature model; not null
	 * @param configName the name of the configuration; not null
	 * @param factory the factory used to construct explanation creators; not null
	 * @param iterations number of iterations to run
	 */
	protected ConfigurationExplanationTest(String projectName, String fmName, String configName, ConfigurationExplanationCreatorFactory factory, int iterations) {
		super(projectName, fmName, configName, iterations);
		this.factory = factory;
	}

	@Override
	public String getName() {
		return String.format("%s_%s_%s_%d",
				getProjectName(),
				getConfigurationName(),
				getOracleName(),
				getIterations());
	}

	/**
	 * Returns the name of the oracle.
	 * 
	 * @return the oracle name; not null
	 */
	private String getOracleName() {
		if (factory instanceof LtmsConfigurationExplanationCreatorFactory) {
			return "LTMS";
		} else if (factory instanceof MusConfigurationExplanationCreatorFactory) {
			return "MUS";
		} else if (factory instanceof CompositeConfigurationExplanationCreatorFactory) {
			return "composite";
		} else {
			throw new IllegalStateException("Unknown oracle");
		}
	}

	@Override
	protected void runBeforeTest() {
		super.runBeforeTest();
		asc = factory.getAutomaticSelectionExplanationCreator();
		asc.setFeatureModel(getFeatureModel());
		asc.setConfiguration(getConfiguration());
	}

	@Override
	protected void runBeforeIterations() {
		super.runBeforeIterations();
		si = getConfiguration().getFeatures().iterator();
		ass = 0;
		asExpl = 0;
		reasons = 0;
	}

	@Override
	protected boolean hasNextStep() {
		while (si.hasNext()) {
			final SelectableFeature s = si.next();
			switch (s.getAutomatic()) {
			case SELECTED:
			case UNSELECTED:
				ass++;
				asc.setSubject(s);
				return true;
			case UNDEFINED:
				continue;
			default:
				throw new IllegalStateException("Unknown selection state");
			}
		}
		return false;
	}

	@Override
	protected Explanation<?> runStep() {
		return asc.getExplanation();
	}

	@Override
	protected void runAfterTest() {
		super.runAfterTest();
		asc = null;
		si = null;
	}

	@Override
	protected Map<String, Object> getStepMeasurement() {
		final Map<String, Object> measurement = super.getStepMeasurement();
		final Explanation<?> explanation = getResult();
		final int reasons;
		final int explanations;
		if (explanation == null) {
			reasons = 0;
			explanations = 0;
		} else {
			asExpl++;
			reasons = explanation.getReasonCount();
			explanations = explanation.getExplanationCount();
			this.reasons += reasons;
		}
		measurement.put("Reasons", reasons);
		measurement.put("Explanations", explanations);
		return measurement;
	}

	@Override
	public Map<String, Object> getTestResults() {
		final Map<String, Object> results = super.getTestResults();
		results.put("Oracle", getOracleName());
		results.put("ASs", ass);
		results.put("ASs Ex", asExpl);
		results.put("Avg Reasons", (double) reasons / Math.max(1, asExpl));
		return results;
	}

	/**
	 * Runs the tests.
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		final List<ConfigurationExplanationCreatorFactory> factories = Arrays.asList(LTMS, MUS, COMPOSITE);
		final List<Test> tests = new LinkedList<>();
		for (final ConfigurationExplanationCreatorFactory factory : factories) {
			tests.add(new ConfigurationExplanationTest("SortingLine", "model", "00012", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("PPU", "model", "00006", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("Violet", "model", "00033", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("uClibc", "model", "00019", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("E-Shop", "model", "00042", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("WaterlooGenerated", "model", "00270", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("Busybox_1.18.0", "model", "00102", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("XSEngine", "model", "00167", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("uClibc-Distribution", "model", "01337", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("Automotive01", "model", "02017", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("PROFilE-ERP-System", "model", "10001", factory, ITERATIONS));
			tests.add(new ConfigurationExplanationTest("PROFilE-E-Agribusiness", "model", "34819", factory, ITERATIONS));
		}
		new TestRunner().run(tests);
	}
}
