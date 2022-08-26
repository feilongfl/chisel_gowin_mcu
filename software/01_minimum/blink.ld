/* only use flash to store program, no-ram
*/
   
SECTIONS
{
        /* interrupt vectors start at zero */
        . = 0x0;  /* start of flash */

        .text :  {  *(.text)   }

        /* constant data follows code but still in flash */
        .data :
        { 
          *(.data) 
          *(.rom)
        }
}
