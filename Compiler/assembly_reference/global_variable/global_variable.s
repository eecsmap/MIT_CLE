	.file	"global_variable.c"
# GNU C17 (Ubuntu 8.4.0-1ubuntu1~18.04) version 8.4.0 (x86_64-linux-gnu)
#	compiled by GNU C version 8.4.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version isl-0.19-GMP

# GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126098
# options passed:  -imultiarch x86_64-linux-gnu global_variable.c
# -mtune=generic -march=x86-64 -fverbose-asm -fstack-protector-strong
# -Wformat -Wformat-security
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
	.globl	g
	.data
	.align 4
	.type	g, @object
	.size	g, 4
g:
	.long	5
	.text
	.globl	add
	.type	add, @function
add:
.LFB0:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	movl	%edi, -4(%rbp)	# a, a
# global_variable.c:4:     g += a;
	movl	g(%rip), %edx	# g, g.0_1
	movl	-4(%rbp), %eax	# a, tmp95
	addl	%edx, %eax	# g.0_1, _2
	movl	%eax, g(%rip)	# _2, g
# global_variable.c:5:     if (a == g) {
	movl	g(%rip), %eax	# g, g.1_3
# global_variable.c:5:     if (a == g) {
	cmpl	%eax, -4(%rbp)	# g.1_3, a
	jne	.L2	#,
# global_variable.c:6:         return a + g;
	movl	g(%rip), %edx	# g, g.2_4
	movl	-4(%rbp), %eax	# a, tmp96
	addl	%edx, %eax	# g.2_4, _7
	jmp	.L3	#
.L2:
# global_variable.c:8:     a += g;
	movl	g(%rip), %eax	# g, g.3_5
	addl	%eax, -4(%rbp)	# g.3_5, a
# global_variable.c:9:     return g + a;
	movl	g(%rip), %edx	# g, g.4_6
	movl	-4(%rbp), %eax	# a, tmp97
	addl	%edx, %eax	# g.4_6, _7
.L3:
# global_variable.c:10: }
	popq	%rbp	#
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE0:
	.size	add, .-add
	.globl	main
	.type	main, @function
main:
.LFB1:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	subq	$16, %rsp	#,
# global_variable.c:14:     a = 0;
	movl	$0, -8(%rbp)	#, a
# global_variable.c:15:     b = add(a);
	movl	-8(%rbp), %eax	# a, tmp89
	movl	%eax, %edi	# tmp89,
	call	add	#
	movl	%eax, -4(%rbp)	# tmp90, b
# global_variable.c:16:     return 0;
	movl	$0, %eax	#, _5
# global_variable.c:17: }
	leave	
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE1:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 8.4.0-1ubuntu1~18.04) 8.4.0"
	.section	.note.GNU-stack,"",@progbits
