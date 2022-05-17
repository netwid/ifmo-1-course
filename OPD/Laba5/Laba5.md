	ORG	0x5C1
STARTW:	WORD	$RES
ADDR:	WORD	$RES
MASK:	WORD	0xFF
STOPSYM:	WORD	0x0A
START:	IN	5
	AND	#0x40
	BEQ	START
	IN	4
	ST	(ADDR)

	AND	MASK
	CMP	STOPSYM
	BNE	S2
	JUMP	OUTPUT

S2:	IN	5
	AND	#0x40
	BEQ	S2
	LD	(ADDR)
	SWAB
	IN	4
	SWAB
	ST	(ADDR)+

	SWAB
	AND	MASK
	CMP	STOPSYM
	BNE	START

OUTPUT:	LD	STARTW
	ST	ADDR
LOOP1:	LD	(ADDR)
	AND	MASK
	CMP	STOPSYM
	BEQ	EXIT
	CALL	OUTPUTSYM
	LD	(ADDR)+
	SWAB
	AND	MASK
	CMP	STOPSYM
	BEQ	EXIT
	CALL	OUTPUTSYM
	JUMP	LOOP1


EXIT:	HLT

OUTPUTSYM:	CALL	DIV10
	LD	#0x00
	ADD	REMAINDER
	OUT	0x14
	LD	QUOTIENT
	CALL	DIV10
	LD	#0x10
	ADD	REMAINDER
	OUT	0x14
	LD	QUOTIENT
	CALL	DIV10
	LD	#0x20
	ADD	REMAINDER
	OUT	0x14

	LD	#0x0B
	OUT	0x14
	LD	#0x1B
	OUT	0x14
	LD	#0x2B
	OUT	0x14

DIVISIBLE:	WORD	?
REMAINDER:	WORD	?
QUOTIENT:	WORD	?
DIVISOR:	WORD	0xA

DIV10:	ST	DIVISIBLE
	ST	REMAINDER
	CLA
	ST	QUOTIENT
	LD	DIVISIBLE
	CMP	0xA
	BLT	ENDDIV
	HLT
LOOP2:	SUB	DIVISOR
	CMP	0xA
	BLT	ENDDIV
	ST	DIVISIBLE
	ST	REMAINDER
	LD	QUOTIENT
	INC
	ST	QUOTIENT
	LD	DIVISIBLE
	JUMP	LOOP2
ENDDIV:	RET

	ORG	0x628
RES:	WORD	?

; 0xC0 0xD0 0xD1 0xDE 0xE2 0xD0 0x3F 0x0A
; 1100 0000
; 1101 0000
; 1101 0001
; 1101 1101
; 1110 0010
; 1101 0000
; 1111 1100
; 0000 1010