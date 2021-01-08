# neuralnet-vga-analysis
Improving the speed of Visibility Graph Analysis by two orders of magnitude with the help of Neural Nets. This project was conducted as part of the Final Thesis for MSc Architectural Computation, Bartlett, University College London.


# Research Abstract
This paper presents the research conducted with the aim of understanding if new advances in computer science, more specifically a type of supervised, feedforward Artificial Neural Network, a Multilayer Perceptron (MLP) is able to estimate the values of Visibility Graph Analysis (VGA) without the need for expensive calculation.

The overarching hypothesis is that an MLP can be setup in a way that it can be trained to learn the relationship between spatial configuration and the VGA (neighbourhood size and clustering coefficient) derived from it. Two hypotheses are stated: firstly, if such an MLP can be created than it will be able to generate spatial configurations for specific VGA values as inputs (mode A); secondly, the network would be able to generate VGA when presented with spatial configuration faster, compared to current method and with negligible error (mode B).

The hypotheses were tested by creating unique setups of an MLP for each mode, all of which had a different configuration. As each combination of possible setups were tested, the performance of the networks could be compared to each other and to the traditional method of VGA calculation.
Both mode A and mode B was able to achieve satisfying results that prove that an MLP is able to generate –with limitations- configurations based on VGA input and it is able to calculate the neighbourhood size and the clustering coefficient of a 2D layout substantially faster and with negligible error.

All MLPs were created at a generic space, therefore the MLP taught once can be adopted universally to most spaces. The implications of the two systems is that spatial analysis can be integrated into the design process, enabling interactive, instant analysis and the possible deployment of optimisation procedures, for instance a Genetic Algorithm.

Link to a published research paper (page 262, 175.1):
http://www.11ssslisbon.pt/docs/5-methodological-and-technical-developments-05072017.pdf
