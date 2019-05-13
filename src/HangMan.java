import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author stephentwood
 */
public class HangMan extends javax.swing.JFrame {
    class Game{
        private String fileName;
        private String movieName;
        private char [] movieNameHidden;
        private String previousGuesses;
        private int numberOfLines;
        private int numberOfGuesses;
        private boolean isWinner;
        private boolean gameOver;

        /**
         * 
         * @param fileName the txt file used to load the list of movies
         * @param numberOfGuesses the number of guesses you'd like the player to have
         * @throws Exception throws exception if the file cannot be opened
         */
        public Game(String fileName, int numberOfGuesses) throws Exception{
            this.fileName = fileName;
            this.isWinner = false;
            this.numberOfGuesses = numberOfGuesses;
            this.gameOver = false;

            /*
             * Gets the number of lines in the file and then pick a random movie name from the file
             * Sets movieName hidden to have dashes for all the letters, puts spaces as well
             */
            try{
                numberOfLines = countNumberOfLines();
                movieName = pickRandomMovie();
                movieNameHidden = new char[movieName.length()];
                previousGuesses = "";

                for(int c=1; c<=movieName.length(); c++){
                    if(movieName.charAt(c-1) == ' '){
                        movieNameHidden[c-1] = ' ';
                    }
                    else{
                        movieNameHidden[c-1] = '_';
                    }
                }
                messageLabel.setText("Press a letter to take a guess");
                guessesLabel.setText("You have " + numberOfGuesses + " guesses left");
                String tempString = new String(movieNameHidden);
                hiddenWordLabel.setText(tempString);
            }
            catch(Exception e){
                throw e;
            }
        }

        public String getMovieName(){ return movieName; }
        boolean getGameOver(){ return gameOver; }

        public int getGuessesLeft(){
            return numberOfGuesses;
        }

        /**
         * 
         * @return checks if the player has guessed all the characters correctly and returns true if so
         */
        boolean checkWinner(){
            isWinner = true;
            for(int c=0; c<movieName.length(); c++){
                if(movieNameHidden[c] == '_'){
                    isWinner = false;
                }
            }
            return isWinner;        
        }

        /**
         * Tells the player how many guesses they have left and requests a character from them,
         * compares their guess to the letters in the movie title
         * If they guess correctly it says so and replaces the hidden letter with the correct letter in the hidden movie name
         * If they guess incorrectly and their guess hasn't been used before it lowers the incorrect guesses
         * left by 1
         */
        void attemptGuess(String guess){

            boolean correctGuess = false;
            boolean alreadyGuessed = previousGuess(guess.toLowerCase().charAt(0));

            //If user hasn't already guessed the letter it goes through each letter to check if it's a correct guess
            if(!alreadyGuessed){
                for(int c=1; c<=movieName.length(); c++){
                    if(movieName.toLowerCase().charAt(c-1) == guess.toLowerCase().charAt(0)){
                        movieNameHidden[c-1] = movieName.charAt(c-1);
                        correctGuess = true;
                    }
                }
            }
            else{
                messageLabel.setText("You've already guessed that letter, try again...");
            }

            /*
             *If they've guessed correctly it tells the user as such otherwise it tells them they're
             * incorrect and tells them there is no <whatever character they guessed> in the title
             */
            if(correctGuess){
                messageLabel.setText("Good guess! \"" + guess.toLowerCase().charAt(0) + "\" is in the movie name");
            }
            else if(!alreadyGuessed){
                messageLabel.setText("Sorry, the movie doesn't contain \"" + guess.charAt(0) + "\"");
            }

            /*
             * If the user didn't guess correctly and they haven't already guessed the letter
             * subtract 1 from the number of guesses left
             */
            if(!correctGuess && !alreadyGuessed){
                numberOfGuesses--;
            }

            String tempString = new String(movieNameHidden);
            hiddenWordLabel.setText(tempString);
            
            if(numberOfGuesses == 0) this.gameOver = true;
            if(this.gameOver){
                messageLabel.setText("Sorry, you failed to guess correctly, the name of the movie was \"" + movieName + "\"");
                guessesLabel.setText("Game Over");
            }
            if(this.checkWinner()){
                this.gameOver = true;
                messageLabel.setText("Yay! You've gussed correctly. You won!");
                guessesLabel.setText("Press \"New Game\" to start again...");
            }
            else guessesLabel.setText("You have " + numberOfGuesses + " guesses left");
        }

        /**
         * 
         * @param guess this character is used to compare to the characters already guessed by the user
         * @return returns true if the character has already been used
         */
        private boolean previousGuess(char guess){
            boolean previouslyGuessed = false;
            for(int c=1; c<=previousGuesses.length(); c++){
                if(guess == previousGuesses.charAt(c-1)){
                    previouslyGuessed = true;
                    break;
                }
            }
            previousGuesses = previousGuesses.concat(Character.toString(guess));
            return previouslyGuessed;
        }

        /**
         * 
         * @return returns a movie name chosen at random from the filename
         * @throws Exception throws an exception if the file can't be read
         */
        private String pickRandomMovie() throws Exception{
            try{
                InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String [] listOfMovies = new String[numberOfLines];

                for(int c=0; c<numberOfLines; c++){
                    listOfMovies[c] = bufferedReader.readLine();
                }

                int randomMovie = (int) (Math.random() * numberOfLines);
                return listOfMovies[randomMovie];
            }
            catch(Exception e){
                System.out.println("Error reading " + fileName);
                throw e;
            }
        }

        /**
         * 
         * @return number of lines in the txt file
         * @throws Exception throws exception if not able to open txt file
         */
        private int countNumberOfLines() throws Exception{
            try{
                InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                int numberOfLines = 0;

                while(bufferedReader.readLine() != null) numberOfLines++;

                bufferedReader.close();
                return numberOfLines;
            }
            catch(Exception e){
                //System.out.println("Error reading " + fileName);
                throw e;
            }
        }

    }
    
    private Game hangMan;

    /**
     * Creates new form HangMan
     */
    public HangMan() throws Exception {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aButton = new javax.swing.JButton();
        bButton = new javax.swing.JButton();
        cButton = new javax.swing.JButton();
        dButton = new javax.swing.JButton();
        eButton = new javax.swing.JButton();
        fButton = new javax.swing.JButton();
        gButton = new javax.swing.JButton();
        hButton = new javax.swing.JButton();
        iButton = new javax.swing.JButton();
        jButton = new javax.swing.JButton();
        kButton = new javax.swing.JButton();
        lButton = new javax.swing.JButton();
        mButton = new javax.swing.JButton();
        nButton = new javax.swing.JButton();
        oButton = new javax.swing.JButton();
        pButton = new javax.swing.JButton();
        qButton = new javax.swing.JButton();
        rButton = new javax.swing.JButton();
        sButton = new javax.swing.JButton();
        tButton = new javax.swing.JButton();
        uButton = new javax.swing.JButton();
        vButton = new javax.swing.JButton();
        wButton = new javax.swing.JButton();
        xButton = new javax.swing.JButton();
        yButton = new javax.swing.JButton();
        zButton = new javax.swing.JButton();
        dashButton = new javax.swing.JButton();
        colonButton = new javax.swing.JButton();
        apostropheButton = new javax.swing.JButton();
        oneButton = new javax.swing.JButton();
        twoButton = new javax.swing.JButton();
        threeButton = new javax.swing.JButton();
        fourButton = new javax.swing.JButton();
        fiveButton = new javax.swing.JButton();
        sixButton = new javax.swing.JButton();
        sevenButton = new javax.swing.JButton();
        eightButton = new javax.swing.JButton();
        nineButton = new javax.swing.JButton();
        zeroButton = new javax.swing.JButton();
        hiddenWordLabel = new javax.swing.JLabel();
        exclamationButton = new javax.swing.JButton();
        newGameButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();
        guessesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hang Man!");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        aButton.setText("A");
        aButton.setToolTipText(null);
        aButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aButtonActionPerformed(evt);
            }
        });

        bButton.setText("B");
        bButton.setToolTipText(null);
        bButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bButtonActionPerformed(evt);
            }
        });

        cButton.setText("C");
        cButton.setToolTipText(null);
        cButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButtonActionPerformed(evt);
            }
        });

        dButton.setText("D");
        dButton.setToolTipText(null);
        dButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dButtonActionPerformed(evt);
            }
        });

        eButton.setText("E");
        eButton.setToolTipText(null);
        eButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eButtonActionPerformed(evt);
            }
        });

        fButton.setText("F");
        fButton.setToolTipText(null);
        fButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fButtonActionPerformed(evt);
            }
        });

        gButton.setText("G");
        gButton.setToolTipText(null);
        gButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gButtonActionPerformed(evt);
            }
        });

        hButton.setText("H");
        hButton.setToolTipText(null);
        hButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hButtonActionPerformed(evt);
            }
        });

        iButton.setText("I");
        iButton.setToolTipText(null);
        iButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iButtonActionPerformed(evt);
            }
        });

        jButton.setText("J");
        jButton.setToolTipText(null);
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });

        kButton.setText("K");
        kButton.setToolTipText(null);
        kButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButtonActionPerformed(evt);
            }
        });

        lButton.setText("L");
        lButton.setToolTipText(null);
        lButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lButtonActionPerformed(evt);
            }
        });

        mButton.setText("M");
        mButton.setToolTipText(null);
        mButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mButtonActionPerformed(evt);
            }
        });

        nButton.setText("N");
        nButton.setToolTipText(null);
        nButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nButtonActionPerformed(evt);
            }
        });

        oButton.setText("O");
        oButton.setToolTipText(null);
        oButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oButtonActionPerformed(evt);
            }
        });

        pButton.setText("P");
        pButton.setToolTipText(null);
        pButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pButtonActionPerformed(evt);
            }
        });

        qButton.setText("Q");
        qButton.setToolTipText(null);
        qButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qButtonActionPerformed(evt);
            }
        });

        rButton.setText("R");
        rButton.setToolTipText(null);
        rButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rButtonActionPerformed(evt);
            }
        });

        sButton.setText("S");
        sButton.setToolTipText(null);
        sButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sButtonActionPerformed(evt);
            }
        });

        tButton.setText("T");
        tButton.setToolTipText(null);
        tButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonActionPerformed(evt);
            }
        });

        uButton.setText("U");
        uButton.setToolTipText(null);
        uButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uButtonActionPerformed(evt);
            }
        });

        vButton.setText("V");
        vButton.setToolTipText(null);
        vButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vButtonActionPerformed(evt);
            }
        });

        wButton.setText("W");
        wButton.setToolTipText(null);
        wButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wButtonActionPerformed(evt);
            }
        });

        xButton.setText("X");
        xButton.setToolTipText(null);
        xButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xButtonActionPerformed(evt);
            }
        });

        yButton.setText("Y");
        yButton.setToolTipText(null);
        yButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yButtonActionPerformed(evt);
            }
        });

        zButton.setText("Z");
        zButton.setToolTipText(null);
        zButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zButtonActionPerformed(evt);
            }
        });

        dashButton.setText("-");
        dashButton.setToolTipText(null);
        dashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashButtonActionPerformed(evt);
            }
        });

        colonButton.setText(":");
        colonButton.setToolTipText(null);
        colonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colonButtonActionPerformed(evt);
            }
        });

        apostropheButton.setText("'");
        apostropheButton.setToolTipText(null);
        apostropheButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apostropheButtonActionPerformed(evt);
            }
        });

        oneButton.setText("1");
        oneButton.setToolTipText(null);
        oneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneButtonActionPerformed(evt);
            }
        });

        twoButton.setText("2");
        twoButton.setToolTipText(null);
        twoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twoButtonActionPerformed(evt);
            }
        });

        threeButton.setText("3");
        threeButton.setToolTipText(null);
        threeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threeButtonActionPerformed(evt);
            }
        });

        fourButton.setText("4");
        fourButton.setToolTipText(null);
        fourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourButtonActionPerformed(evt);
            }
        });

        fiveButton.setText("5");
        fiveButton.setToolTipText(null);
        fiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveButtonActionPerformed(evt);
            }
        });

        sixButton.setText("6");
        sixButton.setToolTipText(null);
        sixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sixButtonActionPerformed(evt);
            }
        });

        sevenButton.setText("7");
        sevenButton.setToolTipText(null);
        sevenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sevenButtonActionPerformed(evt);
            }
        });

        eightButton.setText("8");
        eightButton.setToolTipText(null);
        eightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightButtonActionPerformed(evt);
            }
        });

        nineButton.setText("9");
        nineButton.setToolTipText(null);
        nineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nineButtonActionPerformed(evt);
            }
        });

        zeroButton.setText("0");
        zeroButton.setToolTipText(null);
        zeroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroButtonActionPerformed(evt);
            }
        });

        hiddenWordLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        hiddenWordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hiddenWordLabel.setText("Hang Man!");
        hiddenWordLabel.setToolTipText(null);

        exclamationButton.setText("!");
        exclamationButton.setToolTipText(null);
        exclamationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exclamationButtonActionPerformed(evt);
            }
        });

        newGameButton.setText("New Game");
        newGameButton.setToolTipText(null);
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameButtonActionPerformed(evt);
            }
        });

        messageLabel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setText("Welcome to HangMan, press \"New Game\" to start palying");
        messageLabel.setToolTipText(null);

        guessesLabel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        guessesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guessesLabel.setToolTipText(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(newGameButton)
                    .addComponent(hiddenWordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(aButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(bButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(fButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(gButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(hButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(iButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(oneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(twoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(threeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(fourButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(fiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sixButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sevenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(nineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(zeroButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(kButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(mButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(nButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(oButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(qButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(uButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(vButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(dashButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(colonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(apostropheButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(exclamationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(wButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(xButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(yButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(zButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guessesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(newGameButton)
                .addGap(47, 47, 47)
                .addComponent(hiddenWordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(twoButton)
                    .addComponent(threeButton)
                    .addComponent(fourButton)
                    .addComponent(fiveButton)
                    .addComponent(sixButton)
                    .addComponent(sevenButton)
                    .addComponent(eightButton)
                    .addComponent(nineButton)
                    .addComponent(zeroButton)
                    .addComponent(oneButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aButton)
                    .addComponent(bButton)
                    .addComponent(cButton)
                    .addComponent(dButton)
                    .addComponent(eButton)
                    .addComponent(fButton)
                    .addComponent(gButton)
                    .addComponent(hButton)
                    .addComponent(iButton)
                    .addComponent(jButton)
                    .addComponent(kButton)
                    .addComponent(lButton)
                    .addComponent(mButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oButton)
                    .addComponent(pButton)
                    .addComponent(qButton)
                    .addComponent(rButton)
                    .addComponent(sButton)
                    .addComponent(tButton)
                    .addComponent(uButton)
                    .addComponent(vButton)
                    .addComponent(wButton)
                    .addComponent(xButton)
                    .addComponent(yButton)
                    .addComponent(zButton)
                    .addComponent(nButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exclamationButton)
                    .addComponent(apostropheButton)
                    .addComponent(colonButton)
                    .addComponent(dashButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guessesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("a");
    }//GEN-LAST:event_aButtonActionPerformed

    private void bButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("b");
    }//GEN-LAST:event_bButtonActionPerformed

    private void cButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("c");
    }//GEN-LAST:event_cButtonActionPerformed

    private void dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("d");
    }//GEN-LAST:event_dButtonActionPerformed

    private void eButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("e");
    }//GEN-LAST:event_eButtonActionPerformed

    private void fButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("f");
    }//GEN-LAST:event_fButtonActionPerformed

    private void gButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("g");
    }//GEN-LAST:event_gButtonActionPerformed

    private void hButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("h");
    }//GEN-LAST:event_hButtonActionPerformed

    private void iButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("i");
    }//GEN-LAST:event_iButtonActionPerformed

    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("j");
    }//GEN-LAST:event_jButtonActionPerformed

    private void kButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("k");
    }//GEN-LAST:event_kButtonActionPerformed

    private void lButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("l");
    }//GEN-LAST:event_lButtonActionPerformed

    private void mButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("m");
    }//GEN-LAST:event_mButtonActionPerformed

    private void nButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("n");
    }//GEN-LAST:event_nButtonActionPerformed

    private void oButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("o");
    }//GEN-LAST:event_oButtonActionPerformed

    private void pButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("p");
    }//GEN-LAST:event_pButtonActionPerformed

    private void qButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("q");
    }//GEN-LAST:event_qButtonActionPerformed

    private void rButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("r");
    }//GEN-LAST:event_rButtonActionPerformed

    private void sButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("s");
    }//GEN-LAST:event_sButtonActionPerformed

    private void tButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("t");
    }//GEN-LAST:event_tButtonActionPerformed

    private void uButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("u");
    }//GEN-LAST:event_uButtonActionPerformed

    private void vButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("v");
    }//GEN-LAST:event_vButtonActionPerformed

    private void wButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("w");
    }//GEN-LAST:event_wButtonActionPerformed

    private void xButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("x");
    }//GEN-LAST:event_xButtonActionPerformed

    private void yButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("y");
    }//GEN-LAST:event_yButtonActionPerformed

    private void zButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("z");
    }//GEN-LAST:event_zButtonActionPerformed

    private void dashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("-");
    }//GEN-LAST:event_dashButtonActionPerformed

    private void colonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colonButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess(":");
    }//GEN-LAST:event_colonButtonActionPerformed

    private void apostropheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apostropheButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("'");
    }//GEN-LAST:event_apostropheButtonActionPerformed

    private void oneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("1");
    }//GEN-LAST:event_oneButtonActionPerformed

    private void twoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("2");
    }//GEN-LAST:event_twoButtonActionPerformed

    private void threeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threeButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("3");
    }//GEN-LAST:event_threeButtonActionPerformed

    private void fourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fourButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("4");
    }//GEN-LAST:event_fourButtonActionPerformed

    private void fiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("5");
    }//GEN-LAST:event_fiveButtonActionPerformed

    private void sixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sixButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("6");
    }//GEN-LAST:event_sixButtonActionPerformed

    private void sevenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sevenButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("7");
    }//GEN-LAST:event_sevenButtonActionPerformed

    private void eightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("8");
    }//GEN-LAST:event_eightButtonActionPerformed

    private void nineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nineButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("9");
    }//GEN-LAST:event_nineButtonActionPerformed

    private void zeroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("0");
    }//GEN-LAST:event_zeroButtonActionPerformed

    private void exclamationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exclamationButtonActionPerformed
        if(hangMan != null && !hangMan.getGameOver()) hangMan.attemptGuess("!");
    }//GEN-LAST:event_exclamationButtonActionPerformed

    private void newGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameButtonActionPerformed
        try{
            hangMan = new Game("TXT/movies.txt",10);
        }
        catch(Exception e){
        }
    }//GEN-LAST:event_newGameButtonActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HangMan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HangMan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HangMan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HangMan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    new HangMan().setVisible(true);
                }
                catch(Exception e){}
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aButton;
    private javax.swing.JButton apostropheButton;
    private javax.swing.JButton bButton;
    private javax.swing.JButton cButton;
    private javax.swing.JButton colonButton;
    private javax.swing.JButton dButton;
    private javax.swing.JButton dashButton;
    private javax.swing.JButton eButton;
    private javax.swing.JButton eightButton;
    private javax.swing.JButton exclamationButton;
    private javax.swing.JButton fButton;
    private javax.swing.JButton fiveButton;
    private javax.swing.JButton fourButton;
    private javax.swing.JButton gButton;
    private javax.swing.JLabel guessesLabel;
    private javax.swing.JButton hButton;
    private javax.swing.JLabel hiddenWordLabel;
    private javax.swing.JButton iButton;
    private javax.swing.JButton jButton;
    private javax.swing.JButton kButton;
    private javax.swing.JButton lButton;
    private javax.swing.JButton mButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton nButton;
    private javax.swing.JButton newGameButton;
    private javax.swing.JButton nineButton;
    private javax.swing.JButton oButton;
    private javax.swing.JButton oneButton;
    private javax.swing.JButton pButton;
    private javax.swing.JButton qButton;
    private javax.swing.JButton rButton;
    private javax.swing.JButton sButton;
    private javax.swing.JButton sevenButton;
    private javax.swing.JButton sixButton;
    private javax.swing.JButton tButton;
    private javax.swing.JButton threeButton;
    private javax.swing.JButton twoButton;
    private javax.swing.JButton uButton;
    private javax.swing.JButton vButton;
    private javax.swing.JButton wButton;
    private javax.swing.JButton xButton;
    private javax.swing.JButton yButton;
    private javax.swing.JButton zButton;
    private javax.swing.JButton zeroButton;
    // End of variables declaration//GEN-END:variables
}
