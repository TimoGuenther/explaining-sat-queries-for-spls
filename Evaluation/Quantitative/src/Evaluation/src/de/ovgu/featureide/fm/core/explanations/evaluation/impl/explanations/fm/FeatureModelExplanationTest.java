package de.ovgu.featureide.fm.core.explanations.evaluation.impl.explanations.fm;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.explanations.Explanation;
import de.ovgu.featureide.fm.core.explanations.evaluation.Test;
import de.ovgu.featureide.fm.core.explanations.evaluation.TestRunner;
import de.ovgu.featureide.fm.core.explanations.evaluation.impl.resources.FeatureModelTest;
import de.ovgu.featureide.fm.core.explanations.fm.DeadFeatureExplanation;
import de.ovgu.featureide.fm.core.explanations.fm.DeadFeatureExplanationCreator;
import de.ovgu.featureide.fm.core.explanations.fm.FalseOptionalFeatureExplanation;
import de.ovgu.featureide.fm.core.explanations.fm.FalseOptionalFeatureExplanationCreator;
import de.ovgu.featureide.fm.core.explanations.fm.FeatureModelExplanationCreator;
import de.ovgu.featureide.fm.core.explanations.fm.FeatureModelExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.fm.RedundantConstraintExplanation;
import de.ovgu.featureide.fm.core.explanations.fm.RedundantConstraintExplanationCreator;
import de.ovgu.featureide.fm.core.explanations.fm.impl.composite.CompositeFeatureModelExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.fm.impl.ltms.LtmsFeatureModelExplanationCreatorFactory;
import de.ovgu.featureide.fm.core.explanations.fm.impl.mus.MusFeatureModelExplanationCreatorFactory;

/**
 * Tests explanations for feature model defects.
 * 
 * @author Timo G&uuml;nther
 */
public class FeatureModelExplanationTest extends FeatureModelTest<Explanation<?>> {

	/** True to test void feature models. */
	private static final boolean TEST_VOID_FEATURE_MODELS = true;
	/** True to test dead features. */
	private static final boolean TEST_DEAD_FEATURES = true;
	/** True to test false-optional features. */
	private static final boolean TEST_FALSEOPTIONAL_FEATURES = true;
	/** True to test redundant constraints. */
	private static final boolean TEST_REDUNDANT_CONSTRAINTS = true;

	/** The LTMS factory. */
	private static final FeatureModelExplanationCreatorFactory LTMS = new LtmsFeatureModelExplanationCreatorFactory();
	/** The MUS factory */
	private static final FeatureModelExplanationCreatorFactory MUS = new MusFeatureModelExplanationCreatorFactory();
	/** The composite factory. */
	private static final FeatureModelExplanationCreatorFactory COMPOSITE = new CompositeFeatureModelExplanationCreatorFactory();

	/** The factory used to construct explanation creators. */
	private final FeatureModelExplanationCreatorFactory factory;

	/** The explanation creator for dead features. */
	private DeadFeatureExplanationCreator dfc;
	/** The explanation creator for false-optional features. */
	private FalseOptionalFeatureExplanationCreator fofc;
	/** The explanation creator for redundant constraints. */
	private RedundantConstraintExplanationCreator rcc;
	/** The explanation creator in use in the current step. */
	private FeatureModelExplanationCreator<?, ?> creator;
	
	/** Iterates over features in the feature model. */
	private Iterator<IFeature> fi;
	/** Iterates over constraints in the feature model. */
	private Iterator<IConstraint> ci;

	/** The number of void feature models found. */
	private int vfms;
	/** The number of void feature models explained. */
	private int vfmsExpl;
	/** The number of dead features found. */
	private int dfs;
	/** The number of dead features explained. */
	private int dfsExpl;
	/** The number of false-optional features found. */
	private int fofs;
	/** The number of false-optional features explained. */
	private int fofsExpl;
	/** The number of redundant constraints found. */
	private int rcs;
	/** The number of redundant constraints explained. */
	private int rcsExpl;

	/** The total number of reasons of all found explanations. */
	private int reasons;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param projectName the name of the project; not null
	 * @param fmName the name of the feature model; not null
	 * @param factory the factory used to construct explanation creators; not null
	 * @param iterations number of iterations to run
	 */
	public FeatureModelExplanationTest(String projectName, String fmName, FeatureModelExplanationCreatorFactory factory, int iterations) {
		super(projectName, fmName, iterations);
		this.factory = factory;
	}

	@Override
	public String getName() {
		return String.format("%s_%s_%s_%d",
				getProjectName(),
				getFeatureModelName(),
				getOracleName(),
				getIterations());
	}

	/**
	 * Returns the name of the oracle.
	 * 
	 * @return the oracle name; not null
	 */
	private String getOracleName() {
		if (factory instanceof LtmsFeatureModelExplanationCreatorFactory) {
			return "LTMS";
		} else if (factory instanceof MusFeatureModelExplanationCreatorFactory) {
			return "MUS";
		} else if (factory instanceof CompositeFeatureModelExplanationCreatorFactory) {
			return "composite";
		} else {
			throw new IllegalStateException("Unknown oracle");
		}
	}

	@SuppressWarnings("unused")
	@Override
	protected void runBeforeTest() {
		super.runBeforeTest();
		final IFeatureModel fm = getFeatureModel();
		dfc = factory.getDeadFeatureExplanationCreator();
		fofc = factory.getFalseOptionalFeatureExplanationCreator();
		rcc = factory.getRedundantConstraintExplanationCreator();
		dfc.setFeatureModel(fm);
		fofc.setFeatureModel(fm);
		rcc.setFeatureModel(fm);
		final FeatureModelAnalyzer a = fm.getAnalyser();
		a.calculateFeatures = TEST_VOID_FEATURE_MODELS || TEST_DEAD_FEATURES || TEST_FALSEOPTIONAL_FEATURES || TEST_REDUNDANT_CONSTRAINTS;
		a.calculateConstraints = TEST_REDUNDANT_CONSTRAINTS;
		a.calculateDeadConstraints = TEST_VOID_FEATURE_MODELS || TEST_DEAD_FEATURES;
		a.calculateFOConstraints = TEST_FALSEOPTIONAL_FEATURES;
		a.calculateRedundantConstraints = TEST_REDUNDANT_CONSTRAINTS;
		a.calculateTautologyConstraints = TEST_REDUNDANT_CONSTRAINTS;
		a.analyzeFeatureModel(null);
	}

	@Override
	protected void runBeforeIterations() {
		super.runBeforeIterations();
		vfms = 0;
		vfmsExpl = 0;
		dfs = 0;
		dfsExpl = 0;
		fofs = 0;
		fofsExpl = 0;
		rcs = 0;
		rcsExpl = 0;
		reasons = 0;
	}

	@Override
	protected void runBeforeIteration() {
		super.runBeforeIteration();
		final IFeatureModel fm = getFeatureModel();
		fi = fm.getFeatures().iterator();
		ci = fm.getConstraints().iterator();
	}

	@Override
	protected boolean hasNextStep() {
		while (fi.hasNext()) {
			final IFeature f = fi.next();
			if (!getFeatureModel().getAnalyser().valid()) {
				if (f.getStructure().isRoot()) {
					dfc.setSubject(f);
					creator = dfc;
					if (TEST_VOID_FEATURE_MODELS) {
						vfms++;
						return true;
					}
				} else {
					continue;
				}
			}
			switch (f.getProperty().getFeatureStatus()) {
			case DEAD:
				dfc.setSubject(f);
				creator = dfc;
				if (TEST_DEAD_FEATURES) {
					dfs++;
					return true;
				}
				break;
			case FALSE_OPTIONAL:
				fofc.setSubject(f);
				creator = fofc;
				if (TEST_FALSEOPTIONAL_FEATURES) {
					fofs++;
					return true;
				}
				break;
			default:
				break;
			}
		}
		while (ci.hasNext()) {
			final IConstraint c = ci.next();
			switch (c.getConstraintAttribute()) {
			case IMPLICIT:
			case REDUNDANT:
				rcc.setSubject(c);
				creator = rcc;
				if (TEST_REDUNDANT_CONSTRAINTS) {
					rcs++;
					return true;
				}
			default:
				break;
			}
		}
		return false;
	}

	@Override
	protected Explanation<?> runStep() {
		return creator.getExplanation();
	}

	@Override
	protected void runAfterTest() {
		super.runAfterTest();
		dfc = null;
		fofc = null;
		rcc = null;
		creator = null;
		fi = null;
		ci = null;
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
			if (explanation instanceof DeadFeatureExplanation) {
				if (((DeadFeatureExplanation) explanation).isVoid()) {
					vfmsExpl++;
				} else {
					dfsExpl++;
				}
			} else if (explanation instanceof FalseOptionalFeatureExplanation) {
				fofsExpl++;
			} else if (explanation instanceof RedundantConstraintExplanation) {
				rcsExpl++;
			}
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
		results.put("VFMs", vfms);
		results.put("VFMs Ex", vfmsExpl);
		results.put("DFs", dfs);
		results.put("DFs Ex", dfsExpl);
		results.put("FOFs", fofs);
		results.put("FOFs Ex", fofsExpl);
		results.put("RCs", rcs);
		results.put("RCs Ex", rcsExpl);
		results.put("Avg Reasons", (double) reasons / Math.max(1, vfmsExpl + dfsExpl + fofsExpl + rcsExpl));
		return results;
	}

	/**
	 * Runs the tests.
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		final List<FeatureModelExplanationCreatorFactory> factories = Arrays.asList(LTMS, MUS, COMPOSITE);
		final List<Test> tests = new LinkedList<>();
		for (final String projectName : Arrays.asList(
				"SortingLine",
				"PPU",
				"Violet",
				"uClibc",
				"E-Shop",
				"UClibc-Base",
				"WaterlooGenerated",
				"Busybox_1.18.0",
				"XSEngine",
				"uClibc-Distribution",
				"Automotive01")) {
			for (final FeatureModelExplanationCreatorFactory factory : factories) {
				tests.add(new FeatureModelExplanationTest(projectName, "model", factory, ITERATIONS));
			}
		}
		new TestRunner().run(tests);
	}
}
