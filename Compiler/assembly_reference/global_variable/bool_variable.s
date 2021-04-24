	.file	"bool_variable.cpp"
# GNU C++14 (Ubuntu 8.4.0-1ubuntu1~18.04) version 8.4.0 (x86_64-linux-gnu)
#	compiled by GNU C version 8.4.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version isl-0.19-GMP

# GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126098
# options passed:  -imultiarch x86_64-linux-gnu -D_GNU_SOURCE
# bool_variable.cpp -mtune=generic -march=x86-64 -fverbose-asm
# -fstack-protector-strong -Wformat -Wformat-security
# options enabled:  -fPIC -fPIE -faggressive-loop-optimizations
# -fasynchronous-unwind-tables -fauto-inc-dec -fchkp-check-incomplete-type
# -fchkp-check-read -fchkp-check-write -fchkp-instrument-calls
# -fchkp-narrow-bounds -fchkp-optimize -fchkp-store-bounds
# -fchkp-use-static-bounds -fchkp-use-static-const-bounds
# -fchkp-use-wrappers -fcommon -fdelete-null-pointer-checks
# -fdwarf2-cfi-asm -fearly-inlining -feliminate-unused-debug-types
# -fexceptions -ffp-int-builtin-inexact -ffunction-cse -fgcse-lm
# -fgnu-runtime -fgnu-unique -fident -finline-atomics -fira-hoist-pressure
# -fira-share-save-slots -fira-share-spill-slots -fivopts
# -fkeep-static-consts -fleading-underscore -flifetime-dse
# -flto-odr-type-merging -fmath-errno -fmerge-debug-strings -fpeephole
# -fplt -fprefetch-loop-arrays -freg-struct-return
# -fsched-critical-path-heuristic -fsched-dep-count-heuristic
# -fsched-group-heuristic -fsched-interblock -fsched-last-insn-heuristic
# -fsched-rank-heuristic -fsched-spec -fsched-spec-insn-heuristic
# -fsched-stalled-insns-dep -fschedule-fusion -fsemantic-interposition
# -fshow-column -fshrink-wrap-separate -fsigned-zeros
# -fsplit-ivs-in-unroller -fssa-backprop -fstack-protector-strong
# -fstdarg-opt -fstrict-volatile-bitfields -fsync-libcalls -ftrapping-math
# -ftree-cselim -ftree-forwprop -ftree-loop-if-convert -ftree-loop-im
# -ftree-loop-ivcanon -ftree-loop-optimize -ftree-parallelize-loops=
# -ftree-phiprop -ftree-reassoc -ftree-scev-cprop -funit-at-a-time
# -funwind-tables -fverbose-asm -fzero-initialized-in-bss
# -m128bit-long-double -m64 -m80387 -malign-stringops
# -mavx256-split-unaligned-load -mavx256-split-unaligned-store
# -mfancy-math-387 -mfp-ret-in-387 -mfxsr -mglibc -mieee-fp
# -mlong-double-80 -mmmx -mno-sse4 -mpush-args -mred-zone -msse -msse2
# -mstv -mtls-direct-seg-refs -mvzeroupper

	.text
	.globl	a
	.data
	.type	a, @object
	.size	a, 1
a:
	.byte	1
	.globl	aa
	.type	aa, @object
	.size	aa, 4
aa:
	.byte	1
	.byte	0
	.byte	0
	.byte	1
	.globl	ga
	.align 8
	.type	ga, @object
	.size	ga, 12
ga:
	.long	1
	.long	2
	.long	3
	.globl	g
	.align 4
	.type	g, @object
	.size	g, 4
g:
	.long	3
	.globl	aaa
	.bss
	.align 32
	.type	aaa, @object
	.size	aaa, 800
aaa:
	.zero	800
	.globl	bb
	.type	bb, @object
	.size	bb, 5
bb:
	.zero	5
	.globl	bbb
	.align 32
	.type	bbb, @object
	.size	bbb, 200
bbb:
	.zero	200
	.text
	.globl	_Z3caliiiiiiiiii
	.type	_Z3caliiiiiiiiii, @function
_Z3caliiiiiiiiii:
.LFB0:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	movl	%edi, -4(%rbp)	# a, a
	movl	%esi, -8(%rbp)	# b, b
	movl	%edx, -12(%rbp)	# c, c
	movl	%ecx, -16(%rbp)	# d, d
	movl	%r8d, -20(%rbp)	# e, e
	movl	%r9d, -24(%rbp)	# f, f
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	-4(%rbp), %edx	# a, tmp97
	movl	-8(%rbp), %eax	# b, tmp98
	addl	%eax, %edx	# tmp98, _1
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	-12(%rbp), %eax	# c, tmp99
	addl	%eax, %edx	# tmp99, _2
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	-16(%rbp), %eax	# d, tmp100
	addl	%eax, %edx	# tmp100, _3
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	-20(%rbp), %eax	# e, tmp101
	addl	%eax, %edx	# tmp101, _4
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	-24(%rbp), %eax	# f, tmp102
	addl	%eax, %edx	# tmp102, _5
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	16(%rbp), %eax	# g, tmp103
	addl	%eax, %edx	# tmp103, _6
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	24(%rbp), %eax	# h, tmp104
	addl	%eax, %edx	# tmp104, _7
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	32(%rbp), %eax	# i, tmp105
	addl	%eax, %edx	# tmp105, _8
# bool_variable.cpp:11:     return a + b + c + d + e + f + g + h + i + j;
	movl	40(%rbp), %eax	# j, tmp106
	addl	%edx, %eax	# _8, _19
# bool_variable.cpp:12: }
	popq	%rbp	#
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE0:
	.size	_Z3caliiiiiiiiii, .-_Z3caliiiiiiiiii
	.globl	_Z3calbbbbbbbbbb
	.type	_Z3calbbbbbbbbbb, @function
_Z3calbbbbbbbbbb:
.LFB1:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	pushq	%r12	#
	pushq	%rbx	#
	subq	$80, %rsp	#,
	.cfi_offset 12, -24
	.cfi_offset 3, -32
	movl	%esi, %ebx	# b, tmp94
	movl	%edx, %r11d	# c, tmp96
	movl	%ecx, %r10d	# d, tmp98
	movl	%r8d, %r12d	# e, tmp100
	movl	%r9d, %r8d	# f, tmp102
	movl	16(%rbp), %esi	# g, tmp104
	movl	24(%rbp), %ecx	# h, tmp106
	movl	32(%rbp), %edx	# i, tmp108
	movl	40(%rbp), %eax	# j, tmp110
	movb	%dil, -52(%rbp)	# tmp93, a
	movl	%ebx, %edi	# tmp94, tmp95
	movb	%dil, -56(%rbp)	# tmp95, b
	movl	%r11d, %edi	# tmp96, tmp97
	movb	%dil, -60(%rbp)	# tmp97, c
	movl	%r10d, %edi	# tmp98, tmp99
	movb	%dil, -64(%rbp)	# tmp99, d
	movl	%r12d, %edi	# tmp100, tmp101
	movb	%dil, -68(%rbp)	# tmp101, e
	movl	%r8d, %edi	# tmp102, tmp103
	movb	%dil, -72(%rbp)	# tmp103, f
	movb	%sil, -76(%rbp)	# tmp105, g
	movb	%cl, -80(%rbp)	# tmp107, h
	movb	%dl, -84(%rbp)	# tmp109, i
	movb	%al, -88(%rbp)	# tmp111, j
# bool_variable.cpp:14: bool cal(bool a, bool b, bool c, bool d, bool e, bool f, bool g, bool h, bool i, bool j) {
	movq	%fs:40, %rax	# MEM[(<address-space-1> long unsigned int *)40B], tmp113
	movq	%rax, -24(%rbp)	# tmp113, D.2419
	xorl	%eax, %eax	# tmp113
# bool_variable.cpp:15:     bool k[5] = {true, true, false, false, true};
	movb	$1, -29(%rbp)	#, k
	movb	$1, -28(%rbp)	#, k
	movb	$0, -27(%rbp)	#, k
	movb	$0, -26(%rbp)	#, k
	movb	$1, -25(%rbp)	#, k
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -52(%rbp)	#, a
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -56(%rbp)	#, b
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -60(%rbp)	#, c
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -64(%rbp)	#, d
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -68(%rbp)	#, e
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -72(%rbp)	#, f
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -76(%rbp)	#, g
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -80(%rbp)	#, h
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -84(%rbp)	#, i
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -88(%rbp)	#, j
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movzbl	-28(%rbp), %eax	# k, _1
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	testb	%al, %al	# _1
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movzbl	-27(%rbp), %eax	# k, _2
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	testb	%al, %al	# _2
	je	.L4	#,
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movl	$1, %eax	#, iftmp.0_3
	jmp	.L5	#
.L4:
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movl	$0, %eax	#, iftmp.0_3
.L5:
# bool_variable.cpp:16:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movb	%al, -37(%rbp)	# iftmp.0_3, res
# bool_variable.cpp:17:     const int l = sizeof(k);
	movl	$5, -36(%rbp)	#, l
# bool_variable.cpp:18:     return res;
	movzbl	-37(%rbp), %eax	# res, _24
# bool_variable.cpp:19: }
	movq	-24(%rbp), %rbx	# D.2419, tmp114
	xorq	%fs:40, %rbx	# MEM[(<address-space-1> long unsigned int *)40B], tmp114
	je	.L7	#,
# bool_variable.cpp:19: }
	call	__stack_chk_fail@PLT	#
.L7:
	addq	$80, %rsp	#,
	popq	%rbx	#
	popq	%r12	#
	popq	%rbp	#
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE1:
	.size	_Z3calbbbbbbbbbb, .-_Z3calbbbbbbbbbb
	.globl	main
	.type	main, @function
main:
.LFB2:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
# bool_variable.cpp:22:     cal(1,2,3,4,5,6,7,8,9,10);
	pushq	$10	#
	pushq	$9	#
	pushq	$8	#
	pushq	$7	#
	movl	$6, %r9d	#,
	movl	$5, %r8d	#,
	movl	$4, %ecx	#,
	movl	$3, %edx	#,
	movl	$2, %esi	#,
	movl	$1, %edi	#,
	call	_Z3caliiiiiiiiii	#
	addq	$32, %rsp	#,
# bool_variable.cpp:23:     cal(false,false,false,false,false,false,false,false,false,false);
	pushq	$0	#
	pushq	$0	#
	pushq	$0	#
	pushq	$0	#
	movl	$0, %r9d	#,
	movl	$0, %r8d	#,
	movl	$0, %ecx	#,
	movl	$0, %edx	#,
	movl	$0, %esi	#,
	movl	$0, %edi	#,
	call	_Z3calbbbbbbbbbb	#
	addq	$32, %rsp	#,
# bool_variable.cpp:24:     return 0;
	movl	$0, %eax	#, _4
# bool_variable.cpp:25: }
	leave	
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE2:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 8.4.0-1ubuntu1~18.04) 8.4.0"
	.section	.note.GNU-stack,"",@progbits
