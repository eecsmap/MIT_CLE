	.file	"binary_sort.c"
# GNU C17 (Ubuntu 8.4.0-1ubuntu1~18.04) version 8.4.0 (x86_64-linux-gnu)
#	compiled by GNU C version 8.4.0, GMP version 6.1.2, MPFR version 4.0.1, MPC version 1.1.0, isl version isl-0.19-GMP

# GGC heuristics: --param ggc-min-expand=97 --param ggc-min-heapsize=126103
# options passed:  -imultiarch x86_64-linux-gnu binary_sort.c
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
	.globl	binarySearch
	.type	binarySearch, @function
binarySearch:
.LFB0:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	subq	$48, %rsp	#,
	movq	%rdi, -24(%rbp)	# arr, arr
	movl	%esi, -28(%rbp)	# l, l
	movl	%edx, -32(%rbp)	# r, r
	movl	%ecx, -36(%rbp)	# x, x
# binary_sort.c:5:     if (r >= l) {
	movl	-32(%rbp), %eax	# r, tmp101
	cmpl	-28(%rbp), %eax	# l, tmp101
	jl	.L2	#,
# binary_sort.c:6:         int mid = l + (r - l) / 2;
	movl	-32(%rbp), %eax	# r, tmp102
	subl	-28(%rbp), %eax	# l, _1
# binary_sort.c:6:         int mid = l + (r - l) / 2;
	movl	%eax, %edx	# _1, tmp103
	shrl	$31, %edx	#, tmp103
	addl	%edx, %eax	# tmp103, tmp104
	sarl	%eax	# tmp105
	movl	%eax, %edx	# tmp105, _2
# binary_sort.c:6:         int mid = l + (r - l) / 2;
	movl	-28(%rbp), %eax	# l, tmp109
	addl	%edx, %eax	# _2, tmp108
	movl	%eax, -4(%rbp)	# tmp108, mid
# binary_sort.c:8:         if (arr[mid] == x)
	movl	-4(%rbp), %eax	# mid, tmp110
	cltq
	leaq	0(,%rax,4), %rdx	#, _4
	movq	-24(%rbp), %rax	# arr, tmp111
	addq	%rdx, %rax	# _4, _5
	movl	(%rax), %eax	# *_5, _6
# binary_sort.c:8:         if (arr[mid] == x)
	cmpl	%eax, -36(%rbp)	# _6, x
	jne	.L3	#,
# binary_sort.c:9:             return mid;
	movl	-4(%rbp), %eax	# mid, _13
	jmp	.L4	#
.L3:
# binary_sort.c:11:         if (arr[mid] > x)
	movl	-4(%rbp), %eax	# mid, tmp112
	cltq
	leaq	0(,%rax,4), %rdx	#, _8
	movq	-24(%rbp), %rax	# arr, tmp113
	addq	%rdx, %rax	# _8, _9
	movl	(%rax), %eax	# *_9, _10
# binary_sort.c:11:         if (arr[mid] > x)
	cmpl	%eax, -36(%rbp)	# _10, x
	jge	.L5	#,
# binary_sort.c:12:             return binarySearch(arr, l, mid - 1, x);
	movl	-4(%rbp), %eax	# mid, tmp114
	leal	-1(%rax), %edi	#, _11
	movl	-36(%rbp), %edx	# x, tmp115
	movl	-28(%rbp), %esi	# l, tmp116
	movq	-24(%rbp), %rax	# arr, tmp117
	movl	%edx, %ecx	# tmp115,
	movl	%edi, %edx	# _11,
	movq	%rax, %rdi	# tmp117,
	call	binarySearch	#
	jmp	.L4	#
.L5:
# binary_sort.c:14:         return binarySearch(arr, mid + 1, r, x);
	movl	-4(%rbp), %eax	# mid, tmp118
	leal	1(%rax), %esi	#, _12
	movl	-36(%rbp), %ecx	# x, tmp119
	movl	-32(%rbp), %edx	# r, tmp120
	movq	-24(%rbp), %rax	# arr, tmp121
	movq	%rax, %rdi	# tmp121,
	call	binarySearch	#
	jmp	.L4	#
.L2:
# binary_sort.c:17:     return -1;
	movl	$-1, %eax	#, _13
.L4:
# binary_sort.c:18: }
	leave	
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE0:
	.size	binarySearch, .-binarySearch
	.section	.rodata
	.align 8
.LC0:
	.string	"Element is not present in array"
	.align 8
.LC1:
	.string	"Element is present at index %d"
	.text
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
	subq	$48, %rsp	#,
# binary_sort.c:21: {
	movq	%fs:40, %rax	# MEM[(<address-space-1> long unsigned int *)40B], tmp98
	movq	%rax, -8(%rbp)	# tmp98, D.2455
	xorl	%eax, %eax	# tmp98
# binary_sort.c:23:     arr[0] = 2;
	movl	$2, -32(%rbp)	#, arr
# binary_sort.c:24:     arr[1] = 3;
	movl	$3, -28(%rbp)	#, arr
# binary_sort.c:25:     arr[2] = 4;
	movl	$4, -24(%rbp)	#, arr
# binary_sort.c:26:     arr[3] = 10;
	movl	$10, -20(%rbp)	#, arr
# binary_sort.c:27:     arr[4] = 40;
	movl	$40, -16(%rbp)	#, arr
# binary_sort.c:28:     n = sizeof(arr) / sizeof(arr[0]);
	movl	$5, -44(%rbp)	#, n
# binary_sort.c:29:     x = 10;
	movl	$10, -40(%rbp)	#, x
# binary_sort.c:30:     result = binarySearch(arr, 0, n - 1, x);
	movl	-44(%rbp), %eax	# n, tmp92
	leal	-1(%rax), %esi	#, _1
	movl	-40(%rbp), %edx	# x, tmp93
	leaq	-32(%rbp), %rax	#, tmp94
	movl	%edx, %ecx	# tmp93,
	movl	%esi, %edx	# _1,
	movl	$0, %esi	#,
	movq	%rax, %rdi	# tmp94,
	call	binarySearch	#
	movl	%eax, -36(%rbp)	# tmp95, result
# binary_sort.c:32:                    : printf("Element is present at index %d",
	cmpl	$-1, -36(%rbp)	#, result
	jne	.L7	#,
# binary_sort.c:31:     (result == -1) ? printf("Element is not present in array")
	leaq	.LC0(%rip), %rdi	#,
	movl	$0, %eax	#,
	call	printf@PLT	#
	jmp	.L8	#
.L7:
# binary_sort.c:32:                    : printf("Element is present at index %d",
	movl	-36(%rbp), %eax	# result, tmp96
	movl	%eax, %esi	# tmp96,
	leaq	.LC1(%rip), %rdi	#,
	movl	$0, %eax	#,
	call	printf@PLT	#
.L8:
# binary_sort.c:34:     return 0;
	movl	$0, %eax	#, _17
# binary_sort.c:35: }
	movq	-8(%rbp), %rcx	# D.2455, tmp99
	xorq	%fs:40, %rcx	# MEM[(<address-space-1> long unsigned int *)40B], tmp99
	je	.L10	#,
	call	__stack_chk_fail@PLT	#
.L10:
	leave	
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE1:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 8.4.0-1ubuntu1~18.04) 8.4.0"
	.section	.note.GNU-stack,"",@progbits
