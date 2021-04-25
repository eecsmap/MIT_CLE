	.text
	.section	.rodata
.LC0:
	.string	"%d %d\n"
.LC1:
	.string	"%d %d\n"
	.globl	main
	.type	main, @function
main:
	pushq	%rbp
	movq	%rsp, %rbp
	subq	$48, %rsp
	movq	$10, -8(%rbp) 	# , a
	movq	$20, -16(%rbp) 	# , b
	movq	$30, -24(%rbp) 	# , c
	movq	-8(%rbp), %rdx 	# a, tmp0
	addq	-16(%rbp), %rdx 	# b, tmp0
	movq	%rdx, -32(%rbp) 	# tmp0, d
	movq	-24(%rbp), %rdx 	# c, tmp1
	imulq	$3, %rdx 	# , tmp1
	movq	%rdx, -40(%rbp) 	# tmp1, e
	movq	-32(%rbp), %rdx 	# d, tmp2
	imulq	-40(%rbp), %rdx 	# e, tmp2
	movq	%rdx, %rdx 	# tmp2, tmp3
	subq	$100, %rdx 	# , tmp3
	movq	%rdx, -40(%rbp) 	# tmp3, e
	leaq	.LC0(%rip), %rdi
	movq	-32(%rbp), %rsi 	# d, 
	movq	-40(%rbp), %rdx 	# e, 
	call	printf
	movq	%rax, %rdx 	# , tmp4
	movq	-32(%rbp), %rax 	# d, 
	movq	$16, -32(%rbp) 	# , tmp6
	cqto
	idivq	-32(%rbp) 	# tmp6
	movq	%rdx, %r8 	# , tmp5
	movq	%r8, 8(%rbp) 	# tmp5, g
	movq	-40(%rbp), %rax 	# e, 
	movq	$100, -40(%rbp) 	# , tmp8
	cqto
	idivq	-40(%rbp) 	# tmp8
	movq	%rax, %r8 	# , tmp7
	movq	%r8, 16(%rbp) 	# tmp7, h
	movq	%rdx, -48(%rbp) 	# tmp4, tmp10
	leaq	.LC1(%rip), %rdi
	movq	8(%rbp), %rsi 	# g, 
	movq	16(%rbp), %rdx 	# h, 
	call	printf
	movq	%rax, %r8 	# , tmp9
	movq	-48(%rbp), %rdx 	# tmp10, tmp4
	leave
	ret
