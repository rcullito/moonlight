package com.example.moonlight


/**
 * Constants used throughout the app.
 */

const val startAction = "START"
const val pauseAction = "PAUSE"
const val stopAction = "STOP"

const val rollLowerRotationBound = 2.10
const val rollUpperRotationBound = 2.95
// remember that we measure upright according to the other metric
// than what we are currently attuned to. so the roll section
// would measure upright according to pitch
const val upRightAccordingToPitch = 0.45

const val rollLowest = 0.00
const val rollCutPoint1 = 0.70
const val rollCutPoint2 = 1.37
const val rollCutPoint3 = 2.20
const val rollHighestAndThenSome = 3.15

val rollBack = rollLowest..rollCutPoint1
val rollSideBack = rollCutPoint1..rollCutPoint2
val rollSideStomach = rollCutPoint2..rollCutPoint3
val rollStomach = rollCutPoint3..rollHighestAndThenSome

// stomach bound is only when abs(roll) is closerToPi
const val guessPitchStomachBound = 0.20
// back bound is regardless of roll
const val guessPitchBackBound = 0.80

