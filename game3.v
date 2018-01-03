  module game3(
        CLOCK_50,                                               //      On Board 50 MHz
        KEY,
        SW,
        LEDR,
        LEDG,
        HEX0,
        HEX1,
        HEX2,
        // The ports below are for the VGA output.  Do not change.
        VGA_CLK,                                                //      VGA Clock
        VGA_HS,                                                 //      VGA H_SYNC
        VGA_VS,                                                 //      VGA V_SYNC
        VGA_BLANK_N,                                            //      VGA BLANK
        VGA_SYNC_N,                                             //      VGA SYNC
        VGA_R,                                                  //      VGA Red[9:0]
        VGA_G,                                                  //      VGA Green[9:0]
        VGA_B                                                   //      VGA Blue[9:0]
        );

        // declare all the inputs and outputs
        input   CLOCK_50;                               
        input   [9:0]   SW;
        input   [3:0]   KEY;
        output   [9:0]   LEDR;
        output   [9:0]   LEDG;
        output   [6:0]  HEX0,HEX1,HEX2;

        // Do not change the following outputs
        output          VGA_CLK;                                //      VGA Clock
        output          VGA_HS;                                 //      VGA H_SYNC
        output          VGA_VS;                                 //      VGA V_SYNC
        output          VGA_BLANK_N;                            //      VGA BLANK
        output          VGA_SYNC_N;                             //      VGA SYNC
        output  [9:0]   VGA_R;                                  //      VGA Red[9:0]
        output  [9:0]   VGA_G;                                  //      VGA Green[9:0]
        output  [9:0]   VGA_B;                                  //      VGA Blue[9:0]

        

        // create colour, x and y wire as inputs to vga adapter
        wire [2:0] colour;
        wire [7:0] x;
        wire [6:0] y;
    
        wire [3:0] score;
        wire roundStart;
        wire move;
        wire [23:0] result;
        wire [3:0] hexwire;
        wire [3:0] final;
    


        // Create an Instance of a VGA controller - there can be only one!
        // Define the number of colours as well as the initial background
        // image file (.MIF) for the controller.
        vga_adapter VGA(
                        .resetn(~SW[0]),
                        .clock(CLOCK_50),
                        .colour(colour),
                        .x(x),
                        .y(y),
                        .plot(1'b1),
                        /* Signals for the DAC to drive the monitor. */
                        .VGA_R(VGA_R),
                        .VGA_G(VGA_G),
                        .VGA_B(VGA_B),
                        .VGA_HS(VGA_HS),   
                        .VGA_VS(VGA_VS),
                        .VGA_BLANK(VGA_BLANK_N),
               .VGA_SYNC(VGA_SYNC_N),
                        .VGA_CLK(VGA_CLK));
                defparam VGA.RESOLUTION = "160x120";
                defparam VGA.MONOCHROME = "FALSE";
                defparam VGA.BITS_PER_COLOUR_CHANNEL = 1;
                defparam VGA.BACKGROUND_IMAGE = "black.mif";

    // main control of the game, update all the coordinates using in the game
    control game_control(.clock(CLOCK_50), .start(SW[9]), .roundStart(Enable), .up(~KEY[3]), .down(~KEY[2]), .left(~KEY[1]), .right(~KEY[0]), .x_out(x), .y_out(y), .c_out(colour), .y_display(LEDG[7:0]), .x_display(LEDR[7:0]), .score_display(score),.result_display(final));
    
    // count down to produce the pulse for the game which controls the speed of all the moving objects
    pulse game_pulse(.in(SW[3:2]), .clock(CLOCK_50), .start(SW[1]), .out(result));

    // the display counter and HEX0 are used for testing purposes
    DisplayCounter counter(.enable(Enable), .clear(SW[1]), .out(hexwire));
    hex_decoder h(.hex_digit(hexwire), .segments(HEX0));

    // HEX1 displays the score which player gets during the game from 0 to 9 (9 awards in total)
    hex_decoder h2(.hex_digit(score), .segments(HEX1));

    // HEX2 displays the final result of the game, either "C" for complete or "F" for fail
    hex_decoder h3(.hex_digit(final), .segments(HEX2));

    // connected with the pulse module to produce pulse
    assign Enable = (result == 24'b000000000000000000000001)? 1'b1: 1'b0;

endmodule


module DisplayCounter(enable, clear, out);
    //input load;
    input enable;
    input clear;
    output [3:0] out;
    reg [3:0] q;

    // increase the hex display when recieving an enable signal; used to test if the pulse module functions properly
    always @(posedge enable)
    begin
        if(clear == 1'b0)
            q[3:0] <= 4'b0000;
        else 
            q <= q + 1'b1;
    end
    assign out[3:0] = q[3:0];
endmodule








module control(clock, start, roundStart, up, down, left, right, x_out, y_out, c_out, y_display, x_display, score_display, result_display);
    input clock;
    input start;
    input up, down, left, right;
    input roundStart;
    reg [6:0] state, next;
    reg [7:0] p_x, a0_x, a1_x, a2_x, a3_x, a4_x, a5_x, a6_x, a7_x, a8_x, b0_x, b1_x, b2_x, b3_x, b4_x, b5_x, b6_x, b7_x, b8_x, b9_x, b10_x, b11_x;
    reg [6:0] p_y, a0_y, a1_y, a2_y, a3_y, a4_y, a5_y, a6_y, a7_y, a8_y, b0_y, b1_y, b2_y, b3_y, b4_y, b5_y, b6_y, b7_y, b8_y, b9_y, b10_y, b11_y;
    output reg [7:0] x_out;
    output reg [6:0] y_out;
    output reg [2:0] c_out;
    reg stop;
    output [7:0] y_display;
    output [7:0] x_display;
    output [3:0] score_display;
    output [3:0] result_display;
    reg [3:0] result;
    reg [3:0] score;
    reg done;

    // set up all the initial values for bombs, awards and the player
    localparam  b0_x_init = 8'b00010100, b0_y_init = 7'b0000000, b0_colour = 3'b100, // 20
                b1_x_init = 8'b00101000, b1_y_init = 7'b1111000, b1_colour = 3'b100, // 40
                b2_x_init = 8'b00111100, b2_y_init = 7'b0000000, b2_colour = 3'b100, // 60
                b3_x_init = 8'b01010000, b3_y_init = 7'b1111000, b3_colour = 3'b100, // 80
                b4_x_init = 8'b01100100, b4_y_init = 7'b0000000, b4_colour = 3'b100, // 100
                b5_x_init = 8'b01111000, b5_y_init = 7'b1111000, b5_colour = 3'b100, // 120
                b6_x_init = 8'b10001100, b6_y_init = 7'b0000000, b6_colour = 3'b100, // 140


                b7_x_init = 8'b00000000, b7_y_init = 7'b0010100, b7_colour = 3'b100,
                b8_x_init = 8'b10100000, b8_y_init = 7'b0101000, b8_colour = 3'b100,
                b9_x_init = 8'b00000000, b9_y_init = 7'b0111100, b9_colour = 3'b100,
                b10_x_init = 8'b10100000, b10_y_init = 7'b1010000, b10_colour = 3'b100,
                b11_x_init = 8'b00000000, b11_y_init = 7'b1100100, b11_colour = 3'b100,


                a0_x_init = 8'b00000101, a0_y_init = 7'b0000110, a0_colour = 3'b011, //(5,6)
                a1_x_init = 8'b00100101, a1_y_init = 7'b1000010, a1_colour = 3'b011, //(37,66)
                a2_x_init = 8'b01011001, a2_y_init = 7'b1100010, a2_colour = 3'b011, //(89,98)
                a3_x_init = 8'b10010001, a3_y_init = 7'b1001010, a3_colour = 3'b011, //(145,74)
                a4_x_init = 8'b00010011, a4_y_init = 7'b1100110, a4_colour = 3'b011, //(19,102)
                a5_x_init = 8'b10011000, a5_y_init = 7'b0000101, a5_colour = 3'b011, //(152,5)
                a6_x_init = 8'b01011010, a6_y_init = 7'b0100011, a6_colour = 3'b011, //(90,35)
                a7_x_init = 8'b01111101, a7_y_init = 7'b0110010, a7_colour = 3'b011, //(125,50)
                a8_x_init = 8'b01000110, a8_y_init = 7'b1110011, a8_colour = 3'b011, //(70,115)

                p_x_init = 8'b00110010, p_y_init = 7'b0110010, p_colour = 3'b010; //(50,50)




    localparam  p = 7'd0,
                a0 = 7'd1,
                a1 = 7'd2,
                a2 = 7'd3,
                a3 = 7'd4,
                a4 = 7'd5,
                a5 = 7'd6,
                a6 = 7'd7,
                a7 = 7'd8,
                a8 = 7'd9,
                b0 = 7'd10,
                b1 = 7'd11,
                b2 = 7'd12,
                b3 = 7'd13,
                b4 = 7'd14,
                b5 = 7'd15,
                b6 = 7'd16,
                b7 = 7'd17,
                b8 = 7'd18,
                b9 = 7'd19,
                b10 = 7'd20,
                b11 = 7'd21,
                b0c = 7'd22,
                b0m = 7'd23,
                b1c = 7'd24,
                b1m = 7'd25,
                b2c = 7'd26,
                b2m = 7'd27,
                b3c = 7'd28,
                b3m = 7'd29,
                b4c = 7'd30,
                b4m = 7'd31,
                b5c = 7'd32,
                b5m = 7'd33,
                b6c = 7'd34,
                b6m = 7'd35,
                b7c = 7'd36,
                b7m = 7'd37,
                b8c = 7'd38,
                b8m = 7'd39,
                b9c = 7'd40,
                b9m = 7'd41,
                b10c = 7'd42,
                b10m = 7'd43,
                b11c = 7'd44,
                b11m = 7'd45,
                pc = 7'd46,
                pm = 7'd47,
                collect = 7'd48,
                hit = 7'd49,
                finish = 7'd50;




    always@(*)
    begin
        case(state)
            p: next = a0;               // set up initial positions for all bombs, awards and the player
            a0: next = a1;
            a1: next = a2;
            a2: next = a3;
            a3: next = a4;
            a4: next = a5;
            a5: next = a6;
            a6: next = a7;
            a7: next = a8;
            a8: next = b0;
            b0: next = b1;
            b1: next = b2;
            b2: next = b3;
            b3: next = b4;
            b4: next = b5;
            b5: next = b6;
            b6: next = b7;
            b7: next = b8;
            b8: next = b9;
            b9: next = b10;
            b10: next = b11;
            b11: next = b0c;
            

            b0c: next = b0m;         // start moving bomb0 to bomb11 in order when recieving a pulse signal
            b0m: next = roundStart? b1c:b0m;
            b1c: next = b1m;
            b1m: next = roundStart? b2c:b1m;
            b2c: next = b2m;
            b2m: next = roundStart? b3c:b2m;
            b3c: next = b3m;
            b3m: next = roundStart? b4c:b3m;
            b4c: next = b4m;
            b4m: next = roundStart? b5c:b4m;
            b5c: next = b5m;
            b5m: next = roundStart? b6c:b5m;
            b6c: next = b6m;
            b6m: next = roundStart? b7c:b6m;
            b7c: next = b7m;
            b7m: next = roundStart? b8c:b7m;
            b8c: next = b8m;
            b8m: next = roundStart? b9c:b8m;
            b9c: next = b9m;
            b9m: next = roundStart? b10c:b9m;
            b10c: next = b10m;
            b10m: next = roundStart? b11c:b10m;
            b11c: next = b11m;        
            // if any movement input signal is detected, move the player otherwise keep moving all the bombs for next round
            b11m: next = (up || down || left || right)? pc:collect;
//
            pc: next = pm;
            pm: next = roundStart? collect:pm;
            // after moving the player, detect if the player collects the award or gets hit by the bomb; increase score or stop the game accordingly
            collect: next = stop? finish: hit;
            hit:next = roundStart? finish: hit;
            // if the stop signal is not enabled, start next round of movement; otherwise stay in this state
            finish: begin
                if(stop == 1'b0)begin
                    next <= b0c;
                end
            end

            
        endcase
    end

    always@(posedge clock)
    begin
        case(state)
            p: begin
                x_out <= p_x_init;      //plot the initial position of player
                y_out <= p_y_init;
                c_out <= p_colour;
                p_x <= p_x_init;
                p_y <= p_y_init;
				result <= 4'b0000;
				score <= 4'b0000;
                stop <= 1'b0;
            end
            
            // plot the initial positions for awards
            a0: begin
                x_out <= a0_x_init;
                y_out <= a0_y_init;
                c_out <= a0_colour;
                a0_x <= a0_x_init;
                a0_y <= a0_y_init;
            end
            a1: begin
                x_out <= a1_x_init;
                y_out <= a1_y_init;
                c_out <= a1_colour;
                a1_x <= a1_x_init;
                a1_y <= a1_y_init;
            end
            a2: begin
                x_out <= a2_x_init;
                y_out <= a2_y_init;
                c_out <= a2_colour;
                a2_x <= a2_x_init;
                a2_y <= a2_y_init;
            end
            a3: begin
                x_out <= a3_x_init;
                y_out <= a3_y_init;
                c_out <= a3_colour;
                a3_x <= a3_x_init;
                a3_y <= a3_y_init;
            end
            a4: begin
                x_out <= a4_x_init;
                y_out <= a4_y_init;
                c_out <= a4_colour;
                a4_x <= a4_x_init;
                a4_y <= a4_y_init;
            end
            a5: begin
                x_out <= a5_x_init;
                y_out <= a5_y_init;
                c_out <= a5_colour;
                a5_x <= a5_x_init;
                a5_y <= a5_y_init;
            end
             a6: begin
                x_out <= a6_x_init;
                y_out <= a6_y_init;
                c_out <= a6_colour;
                a6_x <= a6_x_init;
                a6_y <= a6_y_init;
            end
             a7: begin
                x_out <= a7_x_init;
                y_out <= a7_y_init;
                c_out <= a7_colour;
                a7_x <= a7_x_init;
                a7_y <= a7_y_init;
            end
             a8: begin
                x_out <= a8_x_init;
                y_out <= a8_y_init;
                c_out <= a8_colour;
                a8_x <= a8_x_init;
                a8_y <= a8_y_init;
            end
            


            // plot the initial positions for bombs
            b0: begin
                x_out <= b0_x_init;
                y_out <= b0_y_init;
                c_out <= b0_colour;
                b0_x <= b0_x_init;
                b0_y <= b0_y_init;
            end
            b1: begin
                x_out <= b1_x_init;
                y_out <= b1_y_init;
                c_out <= b1_colour;
                b1_x <= b1_x_init;
                b1_y <= b1_y_init;
            end
            b2: begin
                x_out <= b2_x_init;
                y_out <= b2_y_init;
                c_out <= b2_colour;
                b2_x <= b2_x_init;
                b2_y <= b2_y_init;
            end
            b3: begin
                x_out <= b3_x_init;
                y_out <= b3_y_init;
                c_out <= b3_colour;
                b3_x <= b3_x_init;
                b3_y <= b3_y_init;
            end
            b4: begin
                x_out <= b4_x_init;
                y_out <= b4_y_init;
                c_out <= b4_colour;
                b4_x <= b4_x_init;
                b4_y <= b4_y_init;
            end
            b5: begin
                x_out <= b5_x_init;
                y_out <= b5_y_init;
                c_out <= b5_colour;
                b5_x <= b5_x_init;
                b5_y <= b5_y_init;
            end
            b6: begin
                x_out <= b6_x_init;
                y_out <= b6_y_init;
                c_out <= b6_colour;
                b6_x <= b6_x_init;
                b6_y <= b6_y_init;
            end
            b7: begin
                x_out <= b7_x_init;
                y_out <= b7_y_init;
                c_out <= b7_colour;
                b7_x <= b7_x_init;
                b7_y <= b7_y_init;
            end
            b8: begin
                x_out <= b8_x_init;
                y_out <= b8_y_init;
                c_out <= b8_colour;
                b8_x <= b8_x_init;
                b8_y <= b8_y_init;
            end
            b9: begin
                x_out <= b9_x_init;
                y_out <= b9_y_init;
                c_out <= b9_colour;
                b9_x <= b9_x_init;
                b9_y <= b9_y_init;
            end
            b10: begin
                x_out <= b10_x_init;
                y_out <= b10_y_init;
                c_out <= b10_colour;
                b10_x <= b10_x_init;
                b10_y <= b10_y_init;
            end
            b11: begin
                x_out <= b11_x_init;
                y_out <= b11_y_init;
                c_out <= b11_colour;
                b11_x <= b11_x_init;
                b11_y <= b11_y_init;
            end



            // for each bomb movement, colour the current position to black first(clear) and then update the coordinate of corresponding variables
            b0c: begin
                x_out <= b0_x;
                y_out <= b0_y;
                c_out <= 3'b000;
                if(b0_y >= 7'b1111000)begin
                    b0_y <= 7'b0000000;
                end
                else
                begin
                    // moving diagonally
                    b0_y <= b0_y + 7'b0000001;
					b0_x <= b0_x + 8'b00000001;
                end
            end
            // plot the new position of the bomb onto the screen
            b0m: begin
                x_out <= b0_x;
                y_out <= b0_y;
                c_out <= b0_colour;
            end

            

            b1c: begin
                x_out <= b1_x;
                y_out <= b1_y;
                c_out <= 3'b000;
                if(b1_y == 7'b0000000)begin
                    b1_y <= 7'b1111000;
                end
                else
                begin
                    //moving diagonally
                    b1_y <= b1_y -7'b0000001;
                    b1_x <= b1_x + 8'b00000001;
                end
            end         
            b1m: begin
                x_out <= b1_x;
                y_out <= b1_y;
                c_out <= b1_colour;
            end


            b2c: begin
                x_out <= b2_x;
                y_out <= b2_y;
                c_out <= 3'b000;
                if(b2_y == 7'b1111000)begin
                    b2_y <= 7'b0000000;
                end
                else
                begin
                    // moving upwards
                    b2_y <= b2_y + 7'b0000001;
                end
            end
            b2m: begin
                x_out <= b2_x;
                y_out <= b2_y;
                c_out <= b2_colour;
            end


            b3c: begin
                x_out <= b3_x;
                y_out <= b3_y;
                c_out <= 3'b000;
                if(b3_y == 7'b0000000)begin
                    b3_y <= 7'b1111000;
                end
                else
                begin
                    // moving downwards
                    b3_y <= b3_y -7'b0000001;
                end
            end         
            b3m: begin
                x_out <= b3_x;
                y_out <= b3_y;
                c_out <= b3_colour;
            end

            b4c: begin
                x_out <= b4_x;
                y_out <= b4_y;
                c_out <= 3'b000;
                if(b4_y == 7'b1111000)begin
                    b4_y <= 7'b0000000;
                end
                else
                begin
                    b4_y <= b4_y + 7'b0000001;
                end
            end         
            b4m: begin
                x_out <= b4_x;
                y_out <= b4_y;
                c_out <= b4_colour;
            end

            b5c: begin
                x_out <= b5_x;
                y_out <= b5_y;
                c_out <= 3'b000;
                if(b5_y == 7'b0000000)begin
                    b5_y <= 7'b1111000;
                end
                else
                begin
                    b5_y <= b5_y -7'b0000001;
                end
            end         
            b5m: begin
                x_out <= b5_x;
                y_out <= b5_y;
                c_out <= b5_colour;
            end

            b6c: begin
                x_out <= b6_x;
                y_out <= b6_y;
                c_out <= 3'b000;
                if(b6_y == 7'b1111000)begin
                    b6_y <= 7'b0000000;
                end
                else
                begin
                    b6_y <= b6_y + 7'b0000001;
                end
            end         
            b6m: begin
                x_out <= b6_x;
                y_out <= b6_y;
                c_out <= b6_colour;
            end

            b7c: begin
                x_out <= b7_x;
                y_out <= b7_y;
                c_out <= 3'b000;
                if(b7_x == 8'b10100000)begin
                    b7_x <= 8'b00000000;
                end
                else
                begin
                    b7_x <= b7_x + 8'b00000001;
                end
            end
            b7m: begin
                x_out <= b7_x;
                y_out <= b7_y;
                c_out <= b7_colour;
            end
            

            b8c: begin
                x_out <= b8_x;
                y_out <= b8_y;
                c_out <= 3'b000;
                if(b8_x == 8'b00000000)begin
                    b8_x <= 8'b10100000;
                end
                else
                begin
                    b8_x <= b8_x - 8'b00000001;
                end
            end
            b8m: begin
                x_out <= b8_x;
                y_out <= b8_y;
                c_out <= b8_colour;
            end
            

            b9c: begin
                x_out <= b9_x;
                y_out <= b9_y;
                c_out <= 3'b000;
                if(b9_x == 8'b10100000)begin
                    b9_x <= 8'b00000000;
                end
                else
                begin
                    b9_x <= b9_x + 8'b00000001;
                end
            end
            b9m: begin
                x_out <= b9_x;
                y_out <= b9_y;
                c_out <= b9_colour;
            end


            b10c: begin
                x_out <= b10_x;
                y_out <= b10_y;
                c_out <= 3'b000;
                if(b10_x == 8'b00000000)begin
                    b10_x <= 8'b10100000;
                end
                else
                begin
                    b10_x <= b10_x - 8'b00000001;
                end
            end
            b10m: begin
                x_out <= b10_x;
                y_out <= b10_y;
                c_out <= b10_colour;
            end

            b11c: begin
                x_out <= b11_x;
                y_out <= b11_y;
                c_out <= 3'b000;
                if(b11_x == 8'b10100000)begin
                    b11_x <= 8'b00000000;
                end
                else
                begin
                    b11_x <= b11_x + 8'b00000001;
                end
            end
            b11m: begin
                x_out <= b11_x;
                y_out <= b11_y;
                c_out <= b11_colour;
            end

            // if the player gets the moving signal, clear the current position to black first and then update its coordinates according to different direction signals
            pc: begin
                x_out <= p_x;
                y_out <= p_y;
                c_out <= 3'b000;
                if(up == 1'b1)begin
                    if(p_y == 7'b0000000)begin
                        p_y <= 7'b1111000;
                    end
                    else
                    begin
                        p_y <= p_y -7'b0000001;
                    end
                    done = 1'b1;

                end
                else if(down == 1'b1)begin
                    if(p_y == 7'b1111000)begin
                        p_y <= 7'b0000000;
                    end
                    else
                    begin
                        p_y <= p_y + 7'b0000001;
                    end
                    done = 1'b1;
                end
                else if(left == 1'b1)begin
                    if(p_x == 8'b00000000)begin
                        p_x <= 8'b10100000;
                    end
                    else
                    begin
                        p_x <= p_x - 8'b00000001;
                    end
                    done = 1'b1;
                end
                else if(right == 1'b1)begin
                    if(p_x == 8'b10100000)begin
                        p_x <= 8'b00000000;
                    end
                    else
                    begin
                        p_x <= p_x + 8'b00000001;
                    end
                    done = 1'b1;
                end
            end

            pm: begin
                // plot the new position of the player on the screen
                if(done)begin
                x_out <= p_x;
                y_out <= p_y;
                c_out <= p_colour;
                done <= 1'b0;
                end
            end


            collect: begin
                // if the player's coordinates overlap with any of the coordinates for awards, increase the score
                if((p_x == a0_x && p_y == a0_y) || (p_x == a1_x && p_y == a1_y) || (p_x == a2_x && p_y == a2_y) || (p_x == a3_x && p_y == a3_y) || (p_x == a4_x && p_y == a4_y) || (p_x == a5_x && p_y == a5_y) || (p_x == a6_x && p_y == a6_y) || (p_x == a7_x && p_y == a7_y) || (p_x == a8_x && p_y == a8_y)) begin
                    score <= score + 4'b0001;
						  // after collecting all the 9 awards, stop the game and display the result
                          if(score == 4'b1000)begin
                        result <= 4'b1100;
                        stop <= 1'b1;
                    end
                end
            end
            
            // detect if the player's coordinates overlap with any of the coordinates for bombs, if so, game is end and display the final result 
            hit: begin   
                if((p_x == b0_x && p_y == b0_y) || (p_x == b1_x && p_y == b1_y) || (p_x == b2_x && p_y == b2_y) || (p_x == b3_x && p_y == b3_y) || (p_x == b4_x && p_y == b4_y) || (p_x == b5_x && p_y == b5_y) || (p_x == b6_x && p_y == b6_y) || (p_x == b7_x && p_y == b7_y) || (p_x == b8_x && p_y == b8_y) || (p_x == b9_x && p_y == b9_y) || (p_x == b10_x && p_y == b10_y) || (p_x == b11_x && p_y == b11_y)) begin
                  result <= 4'b1111;
                    stop <= 1'b1;
                end
            end

            // once the stop signal is enbled(either hit by a bombs or finish the game), clear the player's display
            finish: begin
					if(stop == 1'b1)begin
                x_out <= p_x;
                y_out <= p_y;
                c_out  <= 3'b000;
					end
            end                

        endcase
    end
    
    
    always@(posedge clock)
     begin
        if(start == 1'b0) begin
            state <= p;
          end
        else
          begin
            state <= next;
          end
    end
     
     // display the changing coordinates of the player, scores and the final result
     assign y_display = {1'b0, p_y};
     assign x_display = p_x;
     assign score_display[3:0] = score[3:0];
     assign result_display[3:0] = result[3:0];


endmodule


module hex_decoder(hex_digit, segments);
    input [3:0] hex_digit;
    output reg [6:0] segments;
   
    always @(*)
        case (hex_digit)
            4'h0: segments = 7'b100_0000;
            4'h1: segments = 7'b111_1001;
            4'h2: segments = 7'b010_0100;
            4'h3: segments = 7'b011_0000;
            4'h4: segments = 7'b001_1001;
            4'h5: segments = 7'b001_0010;
            4'h6: segments = 7'b000_0010;
            4'h7: segments = 7'b111_1000;
            4'h8: segments = 7'b000_0000;
            4'h9: segments = 7'b001_1000;
            4'hA: segments = 7'b000_1000;
            4'hB: segments = 7'b000_0011;
            4'hC: segments = 7'b100_0110;
            4'hD: segments = 7'b010_0001;
            4'hE: segments = 7'b000_0110;
            4'hF: segments = 7'b000_1110;   
            default: segments = 7'h7f;
        endcase
endmodule


module pulse(in, clock, start, out, stop);
    input [1:0] in;
    input clock;
    input start;
    output [23:0] out;
    reg [23:0] q;
    reg [23:0] d;
    input stop;
    
    // acoording to switch inputs, there are three different speeds for moving objects in the game
    always@(*)
    begin
        case(in)
            2'b00: d <= 24'b000000001100010000001000;    
            2'b01: d <= 24'b000000010000000000000000;
            2'b10: d <= 24'b000000100001000010000000;
        endcase
    end
    
    always@(posedge clock)
    begin
        if(!start)begin         //load the selected value
            q <= d;
        end
        else if(q == 24'b000000000000000000000000)begin     // once the countdown reach 0, start over again
            q <= d;
        end
        else
        begin
            q <= q - 1'b1;      // counting down
        end
    end
    assign out[23:0] = q[23:0];     // output the current counting bits to produce pulse signals
endmodule

