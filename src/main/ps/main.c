
#define GPIO_BASE     0x40010000
#define GPIO_DATAOUT  *(volatile unsigned short *)(GPIO_BASE + 0x0004)
#define GPIO_OUTENSET *(volatile unsigned short *)(GPIO_BASE + 0x0010)

void main()
{
	GPIO_OUTENSET |= 0x01;

	while (1) {
		GPIO_DATAOUT ^= 0x01;
	}
}
