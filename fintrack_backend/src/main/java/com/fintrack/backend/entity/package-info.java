/**
 * JPA entity classes for FinTrack Backend.
 *
 * <p>Entities are mapped to PostgreSQL tables via Hibernate.
 * All entities will follow these conventions:
 * <ul>
 *   <li>UUID primary keys (not auto-increment integers) — prevents ID enumeration attacks
 *       and works naturally with Firebase UID-based user identity.</li>
 *   <li>{@code created_at} / {@code updated_at} audit timestamps on every entity.</li>
 *   <li>Optimistic locking via {@code @Version} where contention is possible.</li>
 * </ul>
 *
 * <p>Planned entities (added in later milestones):
 * <ul>
 *   <li>{@code UserProfile} — mirrors Firebase user, stores app-specific profile data</li>
 *   <li>{@code Transaction} — income/expense record with amount, type, category, date, note</li>
 *   <li>{@code Category} — transaction category (e.g. Food, Transport, Salary)</li>
 *   <li>{@code Budget} — monthly category budget with target amount</li>
 *   <li>{@code SavingsGoal} — named goal with target amount and progress</li>
 * </ul>
 */
package com.fintrack.backend.entity;
