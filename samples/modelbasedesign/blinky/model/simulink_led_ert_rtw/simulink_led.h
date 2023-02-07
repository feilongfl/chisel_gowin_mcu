/*
 * File: simulink_led.h
 *
 * Code generated for Simulink model 'simulink_led'.
 *
 * Model version                  : 1.0
 * Simulink Coder version         : 9.8 (R2022b) 13-May-2022
 * C/C++ source code generated on : Wed Feb  8 07:11:55 2023
 *
 * Target selection: ert.tlc
 * Embedded hardware selection: Intel->x86-64 (Windows64)
 * Code generation objectives: Unspecified
 * Validation result: Not run
 */

#ifndef RTW_HEADER_simulink_led_h_
#define RTW_HEADER_simulink_led_h_
#ifndef simulink_led_COMMON_INCLUDES_
#define simulink_led_COMMON_INCLUDES_
#include "rtwtypes.h"
#endif                                 /* simulink_led_COMMON_INCLUDES_ */

#include "simulink_led_types.h"

/* Macros for accessing real-time model data structure */
#ifndef rtmGetErrorStatus
#define rtmGetErrorStatus(rtm)         ((rtm)->errorStatus)
#endif

#ifndef rtmSetErrorStatus
#define rtmSetErrorStatus(rtm, val)    ((rtm)->errorStatus = (val))
#endif

/* Block states (default storage) for system '<Root>' */
typedef struct {
  int32_T clockTickCounter;            /* '<Root>/led_pluse' */
} DW_simulink_led_T;

/* External outputs (root outports fed by signals with default storage) */
typedef struct {
  real_T out_led;                      /* '<Root>/out_led' */
} ExtY_simulink_led_T;

/* Real-time Model Data Structure */
struct tag_RTM_simulink_led_T {
  const char_T * volatile errorStatus;
};

/* Block states (default storage) */
extern DW_simulink_led_T simulink_led_DW;

/* External outputs (root outports fed by signals with default storage) */
extern ExtY_simulink_led_T simulink_led_Y;

/* Model entry point functions */
extern void simulink_led_initialize(void);
extern void simulink_led_step(void);
extern void simulink_led_terminate(void);

/* Real-time Model object */
extern RT_MODEL_simulink_led_T *const simulink_led_M;

/*-
 * The generated code includes comments that allow you to trace directly
 * back to the appropriate location in the model.  The basic format
 * is <system>/block_name, where system is the system number (uniquely
 * assigned by Simulink) and block_name is the name of the block.
 *
 * Use the MATLAB hilite_system command to trace the generated code back
 * to the model.  For example,
 *
 * hilite_system('<S3>')    - opens system 3
 * hilite_system('<S3>/Kp') - opens and selects block Kp which resides in S3
 *
 * Here is the system hierarchy for this model
 *
 * '<Root>' : 'simulink_led'
 */
#endif                                 /* RTW_HEADER_simulink_led_h_ */

/*
 * File trailer for generated code.
 *
 * [EOF]
 */
