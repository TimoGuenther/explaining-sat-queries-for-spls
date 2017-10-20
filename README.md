# Explaining Satisfiability Queries for Software Product Lines

This repository contains artifacts used in the 2017 master's thesis <i>Explaining Satisfiability Queries for Software Product Lines</i> \[Gün17]. In that thesis, an algorithm capable of finding explanations for satisfiability queries in a software product line context is presented. While the implementation of that algorithm is available in the open-source project [FeatureIDE](https://github.com/FeatureIDE/FeatureIDE) \[MTS<sup>+</sup>17], the documentation and the evaluation reference several models that can be accessed from here. Additionally, the code for the evaluation lives in this repository.

## Contents

This repository contains various projects that can be imported into Eclipse.

### Data

All data projects contain feature models that can be opened in [FeatureIDE](https://github.com/FeatureIDE/FeatureIDE) \[MTS<sup>+</sup>17].

* The folder [Documentation/data](Documentation/data) contains the data shown throughout the thesis. All files in that folder are the work of the author, with two exceptions:
  * The feature model of [Car](Documentation/data/Car) is from [Ananieva](https://www.isf.cs.tu-bs.de/data/TestFeatureModels.zip) \[Ana16].
  * The project [HelloWorld-Colligens](Documentation/data/HelloWorld-Colligens) is a FeatureIDE example \[MTS<sup>+</sup>17]. An additional preprocessor annotation was added to [main.c](Documentation/data/HelloWorld-Colligens/src/main.c) in order to showcase explanations for invariant presence conditions.
* The folder [Evaluation/Qualitative/data](Evaluation/Qualitative/data) contains feature models used in the qualitative analysis. All of them are from [Ananieva](https://www.isf.cs.tu-bs.de/data/TestFeatureModels.zip) \[Ana16].
* The folder [Evaluation/Quantitative/data](Evaluation/Quantitative/data) contains the data used in the quantitative analysis. They are sourced from various works:
  * The feature model of [Automotive01](Evaluation/Quantitative/data/Automotive01) is a FeatureIDE example \[MTS<sup>+</sup>17]. Its configurations were generated using IncLing \[AHKT<sup>+</sup>16].
  * The feature models of [SortingLine](Evaluation/Quantitative/data/SortingLine) and [PPU](Evaluation/Quantitative/data/PPU) are from the Pick-and-Place unit case studies \[FLVH15]. Their configurations were generated using IncLing \[AHKT<sup>+</sup>16].
  * The feature models and configurations of [PROFilE-E-Agribusiness](Evaluation/Quantitative/data/PROFilE-E-Agribusiness) and [PROFilE-ERP-System](Evaluation/Quantitative/data/PROFilE-ERP-System) are from [Pereira et al.](http://wwwiti.cs.uni-magdeburg.de/~jualves/PROFilE/) \[PMK<sup>+</sup>16].
  * The remaining projects in that folder are from [Knüppel et al.](https://github.com/AlexanderKnueppel/is-there-a-mismatch) \[KTM<sup>+</sup>17]. Their configurations were generated using IncLing \[AHKT<sup>+</sup>16].

### Sources

The folder [Evaluation/Quantitative/src](Evaluation/Quantitative/src) contains the source code for the quantitative analysis. To run the evaluation program, both this repository and the [FeatureIDE](https://github.com/FeatureIDE/FeatureIDE) repository need to be cloned. The easiest way to get the program working is to import the source projects of both repositories into Eclipse without copying them into the workspace. Then, the main methods of [FeatureModelExplanationTest](Evaluation/Quantitative/src/Evaluation/src/de/ovgu/featureide/fm/core/explanations/evaluation/impl/explanations/fm/FeatureModelExplanationTest.java) and [ConfigurationExplanationTest](Evaluation/Quantitative/src/Evaluation/src/de/ovgu/featureide/fm/core/explanations/evaluation/impl/explanations/config/ConfigurationExplanationTest.java) can be run to gather data on explanations for feature models and configurations respectively. The measurements are stored as comma-separated values in a folder called `results`.

## References

* <b>\[AHKT<sup>+</sup>16]</b> Mustafa Al-Hajjaji, Sebastian Krieter, Thomas Thüm, Malte Lochau, and Gunter Saake. <i>IncLing: Efficient Product-Line Testing Using Incremental Pairwise Sampling</i>. In <i>Proceedings of the International Conference on Generative Programming: Concepts and Experiences (GPCE)</i>, pages 144-155, 2016.
* <b>\[Ana16]</b> Sofia Ananieva. <i>Explaining Defects and Identifying Dependencies in Interrelated Feature Models</i>. Master's thesis, Technische Universität Braunschweig, September 2016.
* <b>\[FLVH15]</b> Stefan Feldmann, Christoph Legat, and Birgit Vogel-Heuser. <i>Engineering Support in the Machine Manufacturing Domain Through Interdisciplinary Product Lines: An Applicability Analysis</i>. In <i>Proceedings of the IFAC Symposium on Information Control Problems in Manufacturing (INCOME)</i>, pages 211-218, 2015.
* <b>\[Gün17]</b> Timo Günther. <i>Explaining Satisfiability Queries for Software Product Lines</i>. Master's thesis, Technische Universität Braunschweig, October 2017.
* <b>\[KTM<sup>+</sup>17]</b> Alexander Knüppel, Thomas Thüm, Stephan Mennicke, Jens Meinicke, and Ina Schaefer. <i>Is There a Mismatch Between Real-World Feature Models and Product-Line Research?</i> In <i>Proceedings of the European Software Engineering Conference/Foundations of Software Engineering (ESECFSE)</i>, pages 291-302, 2017.
* <b>\[MTS<sup>+</sup>17]</b> Jens Meinicke, Thomas Thüm, Reimar Schröter, Fabian Benduhn, Thomas Leich, and Gunter Saake. <i>Mastering Software Variability with FeatureIDE</i>. Springer, 2017.
* <b>\[PMK<sup>+</sup>16]</b> Juliana Alves Pereira, Pawel Matuszyk, Sebastian Krieter, Myra Spiliopoulou, and Gunter Saake. <i>A Feature-Based Personalized Recommender System for Product-Line Configuration</i>. In <i>Proceedings of the International Conference on Generative Programming: Concepts and Experiences (GPCE)</i>, pages 120-131, 2016.
