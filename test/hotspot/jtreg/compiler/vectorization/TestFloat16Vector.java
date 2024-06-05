/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
* @test
* @summary Test vectorization of Float16 binary operations
* @requires vm.compiler2.enabled
* @library /test/lib /
* @compile -XDenablePrimitiveClasses TestFloat16Vector.java
* @run driver compiler.vectorization.TestFloat16Vector
*/

package compiler.vectorization;
import compiler.lib.ir_framework.*;
import java.util.Random;


public class TestFloat16Vector {
    private Float16[] input;
    private Float16[] output;
    private static final int LEN = 2048;
    private Random rng;

    public static void main(String args[]) {
        TestFramework.run(TestFloat16Vector.class);
    }

    public TestFloat16Vector() {
        input  = new Float16[LEN];
        output = new Float16[LEN];
        rng = new Random(42);
        for (int i = 0; i < LEN; ++i) {
            input[i] = Float16.valueOf(Float.floatToFloat16(rng.nextFloat()));
        }
    }

    @Test
    @Warmup(10000)
    @IR(applyIfCPUFeatureOr = {"avx512_fp16", "true", "sve", "true"}, counts = {IRNode.ADD_VHF, ">= 1"})
    @IR(applyIfCPUFeatureAnd = {"fphp", "true", "asimdhp", "true"}, counts = {IRNode.ADD_VHF, ">= 1"})
    public void vectorSumFloat16() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Float16.sum(input[i], input[i]);
        }
        checkResultSum();
    }

    public void checkResultSum() {
        for (int i = 0; i < LEN; ++i) {
            Float16 expected = Float16.sum(input[i], input[i]);
            if (output[i].float16ToRawShortBits() != expected.float16ToRawShortBits()) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i].float16ToRawShortBits() + " != " + expected.float16ToRawShortBits());
            }
        }
    }

    @Test
    @Warmup(10000)
    @IR(applyIfCPUFeature = {"sve", "true"}, counts = {IRNode.SUB_VHF, ">= 1"})
    @IR(applyIfCPUFeatureAnd = {"fphp", "true", "asimdhp", "true"}, counts = {IRNode.SUB_VHF, ">= 1"})
    public void vectorSubFloat16() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Float16.sub(input[i], input[i]);
        }
        checkResultSub();
    }

    public void checkResultSub() {
        for (int i = 0; i < LEN; ++i) {
            Float16 expected = Float16.sub(input[i], input[i]);
            if (output[i].float16ToRawShortBits() != expected.float16ToRawShortBits()) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i].float16ToRawShortBits() + " != " + expected.float16ToRawShortBits());
            }
        }
    }

    @Test
    @Warmup(10000)
    @IR(applyIfCPUFeature = {"sve", "true"}, counts = {IRNode.MUL_VHF, ">= 1"})
    @IR(applyIfCPUFeatureAnd = {"fphp", "true", "asimdhp", "true"}, counts = {IRNode.MUL_VHF, ">= 1"})
    public void vectorMulFloat16() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Float16.mul(input[i], input[i]);
        }
        checkResultMul();
    }

    public void checkResultMul() {
        for (int i = 0; i < LEN; ++i) {
            Float16 expected = Float16.mul(input[i], input[i]);
            if (output[i].float16ToRawShortBits() != expected.float16ToRawShortBits()) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i].float16ToRawShortBits() + " != " + expected.float16ToRawShortBits());
            }
        }
    }

    @Test
    @Warmup(10000)
    @IR(applyIfCPUFeature = {"sve", "true"}, counts = {IRNode.DIV_VHF, ">= 1"})
    @IR(applyIfCPUFeatureAnd = {"fphp", "true", "asimdhp", "true"}, counts = {IRNode.DIV_VHF, ">= 1"})
    public void vectorDivFloat16() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Float16.div(input[i], input[i]);
        }
        checkResultDiv();
    }

    public void checkResultDiv() {
        for (int i = 0; i < LEN; ++i) {
            Float16 expected = Float16.div(input[i], input[i]);
            if (output[i].float16ToRawShortBits() != expected.float16ToRawShortBits()) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i].float16ToRawShortBits() + " != " + expected.float16ToRawShortBits());
            }
        }
    }

    @Test
    @Warmup(10000)
    @IR(applyIfCPUFeature = {"sve", "true"}, counts = {IRNode.MIN_VHF, ">= 1"})
    @IR(applyIfCPUFeatureAnd = {"fphp", "true", "asimdhp", "true"}, counts = {IRNode.MIN_VHF, ">= 1"})
    public void vectorMinFloat16() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Float16.min(input[i], input[i]);
        }
        checkResultMin();
    }

    public void checkResultMin() {
        for (int i = 0; i < LEN; ++i) {
            Float16 expected = Float16.min(input[i], input[i]);
            if (output[i].float16ToRawShortBits() != expected.float16ToRawShortBits()) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i].float16ToRawShortBits() + " != " + expected.float16ToRawShortBits());
            }
        }
    }

    @Test
    @Warmup(10000)
    @IR(applyIfCPUFeature = {"sve", "true"}, counts = {IRNode.MAX_VHF, ">= 1"})
    @IR(applyIfCPUFeatureAnd = {"fphp", "true", "asimdhp", "true"}, counts = {IRNode.MAX_VHF, ">= 1"})
    public void vectorMaxFloat16() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Float16.max(input[i], input[i]);
        }
        checkResultMax();
    }

    public void checkResultMax() {
        for (int i = 0; i < LEN; ++i) {
            Float16 expected = Float16.max(input[i], input[i]);
            if (output[i].float16ToRawShortBits() != expected.float16ToRawShortBits()) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i].float16ToRawShortBits() + " != " + expected.float16ToRawShortBits());
            }
        }
    }
}
