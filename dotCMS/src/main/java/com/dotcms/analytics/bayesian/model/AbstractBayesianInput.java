package com.dotcms.analytics.bayesian.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.List;


/**
 * Bayesian inference calculation input. It consists in 6 known parameters:
 *  <ul>
 *      <li>priors data tp define previous data</li>
 *      <li>control successes: number of successes for control (A)</li>
 *      <li>control failures: number of failures for control (A)</li>
 *      <li>test successes: number of </li>
 *      <li>test failures:</li>
 *  </ul>
 *
 * @author vico
 */
@Value.Style(typeImmutable="*", typeAbstract="Abstract*")
@Value.Immutable
@JsonDeserialize(as = BayesianInput.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface AbstractBayesianInput {

    @JsonProperty("priors")
    BayesianPriors priors();

    @JsonProperty("controlSuccesses")
    long controlSuccesses();

    @JsonProperty("controlFailures")
    long controlFailures();

    @JsonProperty("testSuccesses")
    long testSuccesses();

    @JsonProperty("testFailures")
    long testFailures();

    @JsonProperty("variants")
    List<String> variants();

}
