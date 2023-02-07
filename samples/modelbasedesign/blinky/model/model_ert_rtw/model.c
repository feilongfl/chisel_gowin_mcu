/*
 * File: model.c
 *
 * Code generated for Simulink model 'model'.
 *
 * Model version                  : 1.2
 * Simulink Coder version         : 9.8 (R2022b) 13-May-2022
 * C/C++ source code generated on : Tue Feb  7 20:32:49 2023
 *
 * Target selection: ert.tlc
 * Embedded hardware selection: ARM Compatible->ARM Cortex-M
 * Emulation hardware selection:
 *    Differs from embedded hardware (MATLAB Host)
 * Code generation objectives:
 *    1. RAM efficiency
 *    2. Execution efficiency
 * Validation result: Not run
 */

#include "model.h"
#include "rtwtypes.h"

/* Block signals and states (default storage) */
DW rtDW;

/* External outputs (root outports fed by signals with default storage) */
ExtY rtY;

/* Real-time model */
static RT_MODEL rtM_;
RT_MODEL *const rtM = &rtM_;

/* Model step function */
void model_step(void)
{
  /* Outport: '<Root>/Out1' incorporates:
   *  RelationalOperator: '<Root>/Equal'
   *  UnitDelay: '<S1>/Output'
   */
  rtY.Led = (rtDW.Output_DSTATE >= 500);

  /* Switch: '<S3>/FixPt Switch' incorporates:
   *  Constant: '<S2>/FixPt Constant'
   *  Constant: '<S3>/Constant'
   *  Sum: '<S2>/FixPt Sum1'
   *  UnitDelay: '<S1>/Output'
   */
  if ((uint16_T)(rtDW.Output_DSTATE + 1) > 1000) {
    rtDW.Output_DSTATE = 0U;
  } else {
    rtDW.Output_DSTATE++;
  }

  /* End of Switch: '<S3>/FixPt Switch' */
}

/* Model initialize function */
void model_initialize(void)
{
  /* (no initialization code required) */
}

/*
 * File trailer for generated code.
 *
 * [EOF]
 */
