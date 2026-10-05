/**
 * MapStruct mapper interfaces for FinTrack Backend.
 *
 * <p>Mappers convert between JPA {@code entity} objects and {@code dto} objects.
 * This keeps the persistence model and the API contract decoupled.
 *
 * <p>MapStruct is NOT yet a dependency. Add it in Milestone 3 when the first
 * entity + DTO pair is ready:
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;org.mapstruct&lt;/groupId&gt;
 *   &lt;artifactId&gt;mapstruct&lt;/artifactId&gt;
 *   &lt;version&gt;1.6.x&lt;/version&gt;
 * &lt;/dependency&gt;
 * &lt;!-- annotation processor --&gt;
 * &lt;dependency&gt;
 *   &lt;groupId&gt;org.mapstruct&lt;/groupId&gt;
 *   &lt;artifactId&gt;mapstruct-processor&lt;/artifactId&gt;
 *   &lt;version&gt;1.6.x&lt;/version&gt;
 *   &lt;scope&gt;provided&lt;/scope&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * <p>Planned mappers:
 * <ul>
 *   <li>{@code TransactionMapper}</li>
 *   <li>{@code UserProfileMapper}</li>
 *   <li>{@code BudgetMapper}</li>
 *   <li>{@code SavingsGoalMapper}</li>
 * </ul>
 */
package com.fintrack.backend.mapper;
