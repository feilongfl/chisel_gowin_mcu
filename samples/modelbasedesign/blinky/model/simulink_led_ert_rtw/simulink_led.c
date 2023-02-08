/*
 * File: simulink_led.c
 *
 * Code generated for Simulink model 'simulink_led'.
 *
 * Model version                  : 1.0
 * Simulink Coder version         : 9.8 (R2022b) 13-May-2022
 * C/C++ source code generated on : Wed Feb  8 19:48:40 2023
 *
 * Target selection: ert.tlc
 * Embedded hardware selection: ARM Compatible->ARM Cortex-M
 * Code generation objectives: Unspecified
 * Validation result: Not run
 */

#include "simulink_led.h"
#include "rtwtypes.h"

/* Block states (default storage) */
DW_simulink_led_T simulink_led_DW;

/* External outputs (root outports fed by signals with default storage) */
ExtY_simulink_led_T simulink_led_Y;

/* Real-time model */
static RT_MODEL_simulink_led_T simulink_led_M_;
RT_MODEL_simulink_led_T *const simulink_led_M = &simulink_led_M_;

/* Model step function */
void simulink_led_step(void)
{
  /* Outport: '<Root>/out_led' incorporates:
   *  DiscretePulseGenerator: '<Root>/led_pluse'
   */
  simulink_led_Y.out_led = ((simulink_led_DW.clockTickCounter < 1) &&
    (simulink_led_DW.clockTickCounter >= 0));

  /* DiscretePulseGenerator: '<Root>/led_pluse' */
  if (simulink_led_DW.clockTickCounter >= 1) {
    simulink_led_DW.clockTickCounter = 0;
  } else {
    simulink_led_DW.clockTickCounter++;
  }
}

/* Model initialize function */
void simulink_led_initialize(void)
{
  /* (no initialization code required) */
}

/* Model terminate function */
void simulink_led_terminate(void)
{
  /* (no terminate code required) */
}

/*
 * File trailer for generated code.
 *
 * [EOF]
 */
