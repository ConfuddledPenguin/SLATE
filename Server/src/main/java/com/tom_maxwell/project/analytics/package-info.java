/**
 * The analytic sub=system
 *
 * This is interesting and painful to think about.
 *
 * Everything inside here is runnable as a thread or by itself.
 *
 * Everything needs to be a prototype to ensure thread safety
 *
 * Database connections make you want to kill yourself.
 *
 * Welcome to the world of data analysis
 */
package com.tom_maxwell.project.analytics;