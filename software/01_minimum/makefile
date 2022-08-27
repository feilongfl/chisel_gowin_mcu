
all: build/blink.bin.txt

build/blink_fixed.S: blink.S
	mkdir -p build
	cat blink.S | tr ';' '@' >build/blink_fixed.S

build/blink.o: build/blink_fixed.S
	arm-none-eabi-as -mcpu=cortex-m3 -mthumb -mapcs-32 -gstabs -ahls=build/blink.lst -o build/blink.o build/blink_fixed.S

build/blink.elf: build/blink.o
	arm-none-eabi-ld -v -T blink.ld -o build/blink.elf build/blink.o

build/blink.bin: build/blink.elf
	arm-none-eabi-objcopy -O binary build/blink.elf build/blink.bin

build/blink.bin.txt: build/blink.bin
	xxd -eg4 build/blink.bin | cut -d ' ' -f 2-5 | tr '[:space:]' '\n' | grep -v '^$$' | sed -r s/^/\"/ | sed -r 's/$$/",/' > build/blink.bin.txt

clean:
	rm -rv build

.PHONY: all clean
