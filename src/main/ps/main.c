// delay ms
void delay_ms(volatile unsigned int delay_ms) {
  for (delay_ms = (27000000 >> 13) * delay_ms; delay_ms != 0; delay_ms--)
    ;
}

#define GPIO_BASE 0x40010000
#define GPIO_DATAOUT *(unsigned short *)(GPIO_BASE + 0x0004)
#define GPIO_OUTENSET *(unsigned short *)(GPIO_BASE + 0x0010)

#define UART0_BASE 0x40004000
#define UART0_DATA *(unsigned short *)(UART0_BASE + 0x0000)
#define UART0_STATE *(unsigned short *)(UART0_BASE + 0x0004)
#define UART0_CTRL *(unsigned short *)(UART0_BASE + 0x0008)
#define UART0_BAUDDIV *(unsigned short *)(UART0_BASE + 0x0010)

#define UART0_STATE_TXFULL() (UART0_STATE & 0x01)

void print(char *s) {
  while (*s) {
    while (UART0_STATE_TXFULL())
      ;

    // UART0_DATA = *s++;
    s++;
    UART0_DATA = 0x55;
  }
}

void main() {
  GPIO_OUTENSET |= 0x01;

  UART0_BAUDDIV = 27;
  UART0_CTRL |= 0x1;

  while (1) {
    delay_ms(300);

    GPIO_DATAOUT ^= 0x01;

    print("Hello World!\n");
  }
}
