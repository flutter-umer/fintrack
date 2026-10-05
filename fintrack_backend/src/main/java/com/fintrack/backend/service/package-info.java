/**
 * Business logic services for FinTrack Backend.
 *
 * <p>Services contain all business rules and orchestrate repository access.
 * Controllers call services; services call repositories. Services must not
 * depend on HTTP concerns (HttpServletRequest, ResponseEntity, etc.).
 *
 * <p>Planned services (added alongside their features):
 * <ul>
 *   <li>{@code UserProfileService}</li>
 *   <li>{@code TransactionService}</li>
 *   <li>{@code BudgetService}</li>
 *   <li>{@code SavingsGoalService}</li>
 *   <li>{@code DashboardSummaryService} — aggregates data for the dashboard response</li>
 * </ul>
 */
package com.fintrack.backend.service;
