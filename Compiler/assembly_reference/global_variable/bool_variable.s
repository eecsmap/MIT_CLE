	.file	"bool_variable.cpp"
# GNU C++14 (Ubuntu 9.3.0-17ubuntu1~20.04) version 9.3.0 (x86_64-linux-gnu)
#	compiled by GNU C version 9.3.0, GMP version 6.2.0, MPFR version 4.0.2, MPC version 1.1.0, isl version isl-0.22.1-GMP

# GGC heuristics: --param ggc-min-expand=100 --param ggc-min-heapsize=131072
# options passed:  -imultiarch x86_64-linux-gnu -D_GNU_SOURCE
# bool_variable.cpp -mtune=generic -march=x86-64 -fverbose-asm
# -fasynchronous-unwind-tables -fstack-protector-strong -Wformat
# -Wformat-security -fstack-clash-protection -fcf-protection
# options enabled:  -fPIC -fPIE -faggressive-loop-optimizations
# -fassume-phsa -fasynchronous-unwind-tables -fauto-inc-dec -fcommon
# -fdelete-null-pointer-checks -fdwarf2-cfi-asm -fearly-inlining
# -feliminate-unused-debug-types -fexceptions -ffp-int-builtin-inexact
# -ffunction-cse -fgcse-lm -fgnu-runtime -fgnu-unique -fident
# -finline-atomics -fipa-stack-alignment -fira-hoist-pressure
# -fira-share-save-slots -fira-share-spill-slots -fivopts
# -fkeep-static-consts -fleading-underscore -flifetime-dse
# -flto-odr-type-merging -fmath-errno -fmerge-debug-strings -fpeephole
# -fplt -fprefetch-loop-arrays -freg-struct-return
# -fsched-critical-path-heuristic -fsched-dep-count-heuristic
# -fsched-group-heuristic -fsched-interblock -fsched-last-insn-heuristic
# -fsched-rank-heuristic -fsched-spec -fsched-spec-insn-heuristic
# -fsched-stalled-insns-dep -fschedule-fusion -fsemantic-interposition
# -fshow-column -fshrink-wrap-separate -fsigned-zeros
# -fsplit-ivs-in-unroller -fssa-backprop -fstack-clash-protection
# -fstack-protector-strong -fstdarg-opt -fstrict-volatile-bitfields
# -fsync-libcalls -ftrapping-math -ftree-cselim -ftree-forwprop
# -ftree-loop-if-convert -ftree-loop-im -ftree-loop-ivcanon
# -ftree-loop-optimize -ftree-parallelize-loops= -ftree-phiprop
# -ftree-reassoc -ftree-scev-cprop -funit-at-a-time -funwind-tables
# -fverbose-asm -fzero-initialized-in-bss -m128bit-long-double -m64 -m80387
# -malign-stringops -mavx256-split-unaligned-load
# -mavx256-split-unaligned-store -mfancy-math-387 -mfp-ret-in-387 -mfxsr
# -mglibc -mieee-fp -mlong-double-80 -mmmx -mno-sse4 -mpush-args -mred-zone
# -msse -msse2 -mstv -mtls-direct-seg-refs -mvzeroupper

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
	endbr64	
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	subq	$64, %rsp	#,
	movl	%edi, -36(%rbp)	# a, a
	movl	%esi, -40(%rbp)	# b, b
	movl	%edx, -44(%rbp)	# c, c
	movl	%ecx, -48(%rbp)	# d, d
	movl	%r8d, -52(%rbp)	# e, e
	movl	%r9d, -56(%rbp)	# f, f
# bool_variable.cpp:10: int cal(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j) {
	movq	%fs:40, %rax	# MEM[(<address-space-1> long unsigned int *)40B], tmp110
	movq	%rax, -8(%rbp)	# tmp110, D.2366
	xorl	%eax, %eax	# tmp110
# bool_variable.cpp:12:     k[1] = k[a] + k[b];
	movl	-36(%rbp), %eax	# a, tmp96
	cltq
	movl	-32(%rbp,%rax,4), %edx	# k, _1
# bool_variable.cpp:12:     k[1] = k[a] + k[b];
	movl	-40(%rbp), %eax	# b, tmp98
	cltq
	movl	-32(%rbp,%rax,4), %eax	# k, _2
# bool_variable.cpp:12:     k[1] = k[a] + k[b];
	addl	%edx, %eax	# _1, _3
# bool_variable.cpp:12:     k[1] = k[a] + k[b];
	movl	%eax, -28(%rbp)	# _3, k
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	-36(%rbp), %edx	# a, tmp99
	movl	-40(%rbp), %eax	# b, tmp100
	addl	%eax, %edx	# tmp100, _4
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	-44(%rbp), %eax	# c, tmp101
	addl	%eax, %edx	# tmp101, _5
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	-48(%rbp), %eax	# d, tmp102
	addl	%eax, %edx	# tmp102, _6
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	-52(%rbp), %eax	# e, tmp103
	addl	%eax, %edx	# tmp103, _7
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	-56(%rbp), %eax	# f, tmp104
	addl	%eax, %edx	# tmp104, _8
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	16(%rbp), %eax	# g, tmp105
	addl	%eax, %edx	# tmp105, _9
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	24(%rbp), %eax	# h, tmp106
	addl	%eax, %edx	# tmp106, _10
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	32(%rbp), %eax	# i, tmp107
	addl	%eax, %edx	# tmp107, _11
# bool_variable.cpp:13:     return a + b + c + d + e + f + g + h + i + j;
	movl	40(%rbp), %eax	# j, tmp108
	addl	%edx, %eax	# _11, _24
# bool_variable.cpp:14: }
	movq	-8(%rbp), %rcx	# D.2366, tmp111
	xorq	%fs:40, %rcx	# MEM[(<address-space-1> long unsigned int *)40B], tmp111
	je	.L3	#,
	call	__stack_chk_fail@PLT	#
.L3:
	leave	
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
	endbr64	
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	pushq	%rbx	#
	subq	$88, %rsp	#,
	.cfi_offset 3, -24
	movl	%ecx, %eax	# d, tmp93
	movl	%r8d, %ebx	# e, tmp95
	movl	%r9d, %r11d	# f, tmp97
	movl	16(%rbp), %r10d	# g, tmp99
	movl	24(%rbp), %r9d	# h, tmp101
	movl	32(%rbp), %r8d	# i, tmp103
	movl	40(%rbp), %ecx	# j, tmp105
	movb	%dil, -52(%rbp)	# tmp88, a
	movb	%sil, -56(%rbp)	# tmp90, b
	movb	%dl, -60(%rbp)	# tmp92, c
	movb	%al, -64(%rbp)	# tmp94, d
	movl	%ebx, %eax	# tmp95, tmp96
	movb	%al, -68(%rbp)	# tmp96, e
	movl	%r11d, %eax	# tmp97, tmp98
	movb	%al, -72(%rbp)	# tmp98, f
	movl	%r10d, %eax	# tmp99, tmp100
	movb	%al, -76(%rbp)	# tmp100, g
	movl	%r9d, %eax	# tmp101, tmp102
	movb	%al, -80(%rbp)	# tmp102, h
	movl	%r8d, %eax	# tmp103, tmp104
	movb	%al, -84(%rbp)	# tmp104, i
	movl	%ecx, %eax	# tmp105, tmp106
	movb	%al, -88(%rbp)	# tmp106, j
# bool_variable.cpp:16: bool cal(bool a, bool b, bool c, bool d, bool e, bool f, bool g, bool h, bool i, bool j) {
	movq	%fs:40, %rax	# MEM[(<address-space-1> long unsigned int *)40B], tmp108
	movq	%rax, -24(%rbp)	# tmp108, D.2368
	xorl	%eax, %eax	# tmp108
# bool_variable.cpp:17:     bool k[5] = {true, true, false, false, true};
	movb	$1, -29(%rbp)	#, k
	movb	$1, -28(%rbp)	#, k
	movb	$0, -27(%rbp)	#, k
	movb	$0, -26(%rbp)	#, k
	movb	$1, -25(%rbp)	#, k
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -52(%rbp)	#, a
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -56(%rbp)	#, b
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -60(%rbp)	#, c
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -64(%rbp)	#, d
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -68(%rbp)	#, e
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -72(%rbp)	#, f
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -76(%rbp)	#, g
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -80(%rbp)	#, h
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -84(%rbp)	#, i
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	cmpb	$0, -88(%rbp)	#, j
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movzbl	-28(%rbp), %eax	# k, _1
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	testb	%al, %al	# _1
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movzbl	-27(%rbp), %eax	# k, _2
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	testb	%al, %al	# _2
	je	.L5	#,
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movl	$1, %eax	#, iftmp.0_3
	jmp	.L6	#
.L5:
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movl	$0, %eax	#, iftmp.0_3
.L6:
# bool_variable.cpp:18:     bool res = a && b && c && d && e && f && g && h && i && j && k[1] && k[2];
	movb	%al, -37(%rbp)	# iftmp.0_3, res
# bool_variable.cpp:19:     const int l = sizeof(k);
	movl	$5, -36(%rbp)	#, l
# bool_variable.cpp:20:     return res;
	movzbl	-37(%rbp), %eax	# res, _24
# bool_variable.cpp:21: }
	movq	-24(%rbp), %rbx	# D.2368, tmp109
	xorq	%fs:40, %rbx	# MEM[(<address-space-1> long unsigned int *)40B], tmp109
	je	.L8	#,
# bool_variable.cpp:21: }
	call	__stack_chk_fail@PLT	#
.L8:
	addq	$88, %rsp	#,
	popq	%rbx	#
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
	endbr64	
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
# bool_variable.cpp:24:     cal(1,2,3,4,5,6,7,8,9,10);
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
# bool_variable.cpp:25:     cal(false,false,false,false,false,false,false,false,false,false);
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
# bool_variable.cpp:26:     return 0;
	movl	$0, %eax	#, _4
# bool_variable.cpp:27: }
	leave	
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE2:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 9.3.0-17ubuntu1~20.04) 9.3.0"
	.section	.note.GNU-stack,"",@progbits
	.section	.note.gnu.property,"a"
	.align 8
	.long	 1f - 0f
	.long	 4f - 1f
	.long	 5
0:
	.string	 "GNU"
1:
	.align 8
	.long	 0xc0000002
	.long	 3f - 2f
2:
	.long	 0x3
3:
	.align 8
4:
