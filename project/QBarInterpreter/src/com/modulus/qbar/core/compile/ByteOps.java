package com.modulus.qbar.core.compile;
/**
 * The simple byte code operators needed to
 * run these simple functions.
 * 
 * @author jrahm
 *
 */

/* 
 * STOre
 * CALL
 * PUSH
 * PUSH Object
 * PUSH Hard Find
 * PUSH Reference
 * POP
 * CALL Global
 * IToRator
 */
public enum ByteOps { STO, CALL, PUSH, PUSHO, PUSHHF, PUSHR, POP, CALLG, ITR }
