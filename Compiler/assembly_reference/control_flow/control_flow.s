	.file	"control_flow.c"
# GNU C17 (Ubuntu 8.4.0-1ubuntu1~18.04) version 8.4.0 (x86_64-linux-gnu)
#	compiled by GNU C version 8.4.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version isl-0.19-GMP

# GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126103
# options passed:  -imultiarch x86_64-linux-gnu control_flow.c
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
# control_flow.c:2:     int a = 0, b = 0;
	movl	$0, -4(%rbp)	#, a
# control_flow.c:2:     int a = 0, b = 0;
	movl	$0, -16(%rbp)	#, b
# control_flow.c:3:     if (a) {
	cmpl	$0, -4(%rbp)	#, a
	je	.L2	#,
# control_flow.c:4:         b += 1;
	addl	$1, -16(%rbp)	#, b
.L2:
# control_flow.c:6:     if (a) {
	cmpl	$0, -4(%rbp)	#, a
	je	.L3	#,
# control_flow.c:7:         b += 1;
	addl	$1, -16(%rbp)	#, b
	jmp	.L5	#
.L3:
# control_flow.c:9:         b -= 1;
	subl	$1, -16(%rbp)	#, b
# control_flow.c:11:     while (a) {
	jmp	.L5	#
.L6:
# control_flow.c:12:         b += 1;
	addl	$1, -16(%rbp)	#, b
.L5:
# control_flow.c:11:     while (a) {
	cmpl	$0, -4(%rbp)	#, a
	jne	.L6	#,
# control_flow.c:14:     for (int c = 0; c < 5; c++) {
	movl	$0, -12(%rbp)	#, c
# control_flow.c:14:     for (int c = 0; c < 5; c++) {
	jmp	.L7	#
.L8:
# control_flow.c:15:         b += 1;
	addl	$1, -16(%rbp)	#, b
# control_flow.c:14:     for (int c = 0; c < 5; c++) {
	addl	$1, -12(%rbp)	#, c
.L7:
# control_flow.c:14:     for (int c = 0; c < 5; c++) {
	cmpl	$4, -12(%rbp)	#, c
	jle	.L8	#,
# control_flow.c:17:     for (int c = 0; c < 5; c++) {
	movl	$0, -8(%rbp)	#, c
# control_flow.c:17:     for (int c = 0; c < 5; c++) {
	jmp	.L9	#
.L12:
# control_flow.c:19:             if (a) {
	cmpl	$0, -4(%rbp)	#, a
	je	.L11	#,
# control_flow.c:20:                 b += 1;
	addl	$1, -16(%rbp)	#, b
	jmp	.L10	#
.L11:
# control_flow.c:22:                 b -= 1;
	subl	$1, -16(%rbp)	#, b
.L10:
# control_flow.c:18:         while (a) {
	cmpl	$0, -4(%rbp)	#, a
	jne	.L12	#,
# control_flow.c:17:     for (int c = 0; c < 5; c++) {
	addl	$1, -8(%rbp)	#, c
.L9:
# control_flow.c:17:     for (int c = 0; c < 5; c++) {
	cmpl	$4, -8(%rbp)	#, c
	jle	.L10	#,
# control_flow.c:26:     return 0;
	movl	$0, %eax	#, _22
# control_flow.c:27: }
	popq	%rbp	#
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE0:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 8.4.0-1ubuntu1~18.04) 8.4.0"
	.section	.note.GNU-stack,"",@progbits
