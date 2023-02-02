
PREFIX=arm-none-eabi
CC=$(PREFIX)-cc
AS=$(PREFIX)-as
LD=$(PREFIX)-ld
OBJDUMP=$(PREFIX)-objdump

ASFLAGS=-mcpu=cortex-m3 -mthumb
CFLAGS=-mcpu=cortex-m3 -mthumb -std=gnu11
LDFLAGS=-mcpu=cortex-m3 -mthumb

all: ps.bin

%.o: %.S
	arm-none-eabi-as $(ASFLAGS) -c -g $< -o $@

%.o: %.c
	arm-none-eabi-gcc $(CFLAGS) -c -O3 -g $< -o $@

%.bin: %.elf
	arm-none-eabi-objcopy -O binary $< $@
	arm-none-eabi-objdump -S $< > $@.dis.asm

ps.elf: link.ld main.o boot.o
	arm-none-eabi-ld -Map=ps.map -T $^ -o $@

clean:
	-rm *.o *.elf *.bin *.map
