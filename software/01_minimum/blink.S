;;; blink.S
;;; written by YuLong Yao <feilongphone@gmail.com>
;;; based on: Frank Sergeant
;;;    frank@pygmy.utoh.org
;;;    http://pygmy.utoh.org/riscy
;;; This program is in the public domain.  See http://pygmy.utoh.org/riscy/cortex/ 
;;; for notes about the program and how to assemble, link, and burn to flash.

;;; Blink the LED on the Gowin gw1nsr-4c EMCU IP

;;; Directives
        .thumb                  ; (same as saying '.code 16')
        .syntax unified

;;; Equates
        .equ STACKINIT,   0x20005000

        .equ GPIO_DATA,   0x40010000
        .equ GPIO_DATAOUT,   0x40010004
        .equ GPIO_OUTENSET,   0x40010010
        .equ GPIO_OUTENCLR,   0x40010014
        
        .equ LEDDELAY,    800000
        
.section .text
        .org 0

;;; Vectors
vectors:        
        .word STACKINIT         ; stack pointer value when stack is empty
        .word _start + 1        ; reset vector (manually adjust to odd for thumb)
        .word _nmi_handler + 1  ; 
        .word _hard_fault  + 1  ; 
        .word _memory_fault + 1 ; 
        .word _bus_fault + 1    ; 
        .word _usage_fault + 1  ; 

_start:

        ;; Enable the Port C peripheral clock by setting bit 4
        ldr r6, = GPIO_OUTENSET
        mov r0, 0xffff
        str r0, [r6]

        ;; Load R2 and R3 with the "on" and "off" constants
        mov r2, 0              ; value to turn on LED
        mov r3, 0xffff         ; value to turn off LED
        ldr r6, = GPIO_DATAOUT    ;  point to Port C output data register
        
loop:
        str r2, [r6]           ; clear Port C, pin 12, turning on LED
        ldr r1, = LEDDELAY
delay1:
        subs r1, 1
        bne delay1
        
        str r3, [r6]           ; set Port C, pin 12, turning off LED
        ldr r1, = LEDDELAY
delay2:
        subs r1, 1
        bne delay2

        b loop                 ; continue forever

_dummy:                        ; if any int gets triggered, just hang in a loop
_nmi_handler:
_hard_fault:
_memory_fault:
_bus_fault:
_usage_fault:
        add r0, 1
        add r1, 1
        b _dummy                
