	.file	"binary_ops.c"
# GNU C17 (Ubuntu 8.4.0-1ubuntu1~18.04) version 8.4.0 (x86_64-linux-gnu)
#	compiled by GNU C version 8.4.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version isl-0.19-GMP

# GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126103
# options passed:  -imultiarch x86_64-linux-gnu binary_ops.c -mtune=generic
# -march=x86-64 -fverbose-asm -fstack-protector-strong -Wformat
# -Wformat-security
# options enabled:  -fPIC -fPIE -faggressive-loop-optimizations
# -fasynchronous-unwind-tables -fauto-inc-dec -fchkp-check-incomplete-type
# -fchkp-check-read -fchkp-check-write -fchkp-instrument-calls
# -fchkp-narrow-bounds -fchkp-optimize -fchkp-store-bounds
# -fchkp-use-static-bounds -fchkp-use-static-const-bounds
# -fchkp-use-wrappers -fcommon -fdelete-null-pointer-checks
# -fdwarf2-cfi-asm -fearly-inlining -feliminate-unused-debug-types
# -ffp-int-builtin-inexact -ffunction-cse -fgcse-lm -fgnu-runtime
# -fgnu-unique -fident -finline-atomics -fira-hoist-pressure
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
	.globl	main
	.type	main, @function
main:
.LFB0:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$0, -28(%rbp)	#, a
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$1, -24(%rbp)	#, b
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$2, -20(%rbp)	#, c
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$3, -16(%rbp)	#, d
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$4, -12(%rbp)	#, e
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$5, -8(%rbp)	#, f
# binary_ops.c:3:     a = 0; b = 1; c = 2; d = 3; e = 4; f = 5; g = 6;
	movl	$6, -4(%rbp)	#, g
# binary_ops.c:4:     a = b + c;
	movl	-24(%rbp), %edx	# b, tmp101
	movl	-20(%rbp), %eax	# c, tmp102
	addl	%edx, %eax	# tmp101, tmp100
	movl	%eax, -28(%rbp)	# tmp100, a
# binary_ops.c:5:     b = c - d;
	movl	-20(%rbp), %eax	# c, tmp106
	subl	-16(%rbp), %eax	# d, tmp105
	movl	%eax, -24(%rbp)	# tmp105, b
# binary_ops.c:6:     c++;
	addl	$1, -20(%rbp)	#, c
# binary_ops.c:7:     --d;
	subl	$1, -16(%rbp)	#, d
# binary_ops.c:8:     e = (f == g);
	movl	-8(%rbp), %eax	# f, tmp107
	cmpl	-4(%rbp), %eax	# g, tmp107
	sete	%al	#, _1
# binary_ops.c:8:     e = (f == g);
	movzbl	%al, %eax	# _1, tmp108
	movl	%eax, -12(%rbp)	# tmp108, e
# binary_ops.c:9:     f = (e && g);
	cmpl	$0, -12(%rbp)	#, e
	je	.L2	#,
# binary_ops.c:9:     f = (e && g);
	cmpl	$0, -4(%rbp)	#, g
	je	.L2	#,
# binary_ops.c:9:     f = (e && g);
	movl	$1, %eax	#, iftmp.0_7
	jmp	.L3	#
.L2:
# binary_ops.c:9:     f = (e && g);
	movl	$0, %eax	#, iftmp.0_7
.L3:
# binary_ops.c:9:     f = (e && g);
	movl	%eax, -8(%rbp)	# iftmp.0_7, f
# binary_ops.c:10:     g = (e || f);
	cmpl	$0, -12(%rbp)	#, e
	jne	.L4	#,
# binary_ops.c:10:     g = (e || f);
	cmpl	$0, -8(%rbp)	#, f
	je	.L5	#,
.L4:
# binary_ops.c:10:     g = (e || f);
	movl	$1, %eax	#, iftmp.1_8
	jmp	.L6	#
.L5:
# binary_ops.c:10:     g = (e || f);
	movl	$0, %eax	#, iftmp.1_8
.L6:
# binary_ops.c:10:     g = (e || f);
	movl	%eax, -4(%rbp)	# iftmp.1_8, g
# binary_ops.c:11:     e = (f <= g);
	movl	-8(%rbp), %eax	# f, tmp109
	cmpl	-4(%rbp), %eax	# g, tmp109
	setle	%al	#, _2
# binary_ops.c:11:     e = (f <= g);
	movzbl	%al, %eax	# _2, tmp110
	movl	%eax, -12(%rbp)	# tmp110, e
# binary_ops.c:12:     e = (f >= g);
	movl	-8(%rbp), %eax	# f, tmp111
	cmpl	-4(%rbp), %eax	# g, tmp111
	setge	%al	#, _3
# binary_ops.c:12:     e = (f >= g);
	movzbl	%al, %eax	# _3, tmp112
	movl	%eax, -12(%rbp)	# tmp112, e
# binary_ops.c:13:     e = (f < g);
	movl	-8(%rbp), %eax	# f, tmp113
	cmpl	-4(%rbp), %eax	# g, tmp113
	setl	%al	#, _4
# binary_ops.c:13:     e = (f < g);
	movzbl	%al, %eax	# _4, tmp114
	movl	%eax, -12(%rbp)	# tmp114, e
# binary_ops.c:14:     e = (f > g);
	movl	-8(%rbp), %eax	# f, tmp115
	cmpl	-4(%rbp), %eax	# g, tmp115
	setg	%al	#, _5
# binary_ops.c:14:     e = (f > g);
	movzbl	%al, %eax	# _5, tmp116
	movl	%eax, -12(%rbp)	# tmp116, e
# binary_ops.c:15:     e = !f;
	cmpl	$0, -8(%rbp)	#, f
	sete	%al	#, _6
# binary_ops.c:15:     e = !f;
	movzbl	%al, %eax	# _6, tmp117
	movl	%eax, -12(%rbp)	# tmp117, e
# binary_ops.c:16:     e = -f;
	movl	-8(%rbp), %eax	# f, tmp121
	negl	%eax	# tmp120
	movl	%eax, -12(%rbp)	# tmp120, e
# binary_ops.c:17:     return 0;
	movl	$0, %eax	#, _33
# binary_ops.c:18: }
	popq	%rbp	#
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE0:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 8.4.0-1ubuntu1~18.04) 8.4.0"
	.section	.note.GNU-stack,"",@progbits
