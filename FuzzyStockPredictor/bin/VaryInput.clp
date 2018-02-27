(import nrc.fuzzy.*)

(import nrc.fuzz.jess.*)

(load-package nrc.fuzzy.jess.FuzzyFunctions)

;(deftemplate Data (declare (from-class indicatorsCalculations.Data)))

(deftemplate Data
    (slot id)
    (slot macd)
    (slot macd_trigger)
    (slot obv)
    (slot rsi)
    (slot so)
    (slot result)
    )


(deftemplate Data_rsi
    "Auto-generated"
	(declare (ordered TRUE)))
(deftemplate Data_so
    "Auto-generated"
	(declare (ordered TRUE)))

(defglobal ?*rsi* = (new FuzzyVariable "rsi" 0 200))
(defglobal ?*so* = (new FuzzyVariable "so" 0 200))

(defglobal ?*macd_flag* = "")
(defglobal ?*rsi_flag* = "")
(defglobal ?*so_flag* = "")
(defglobal ?*obv_flag* = "")

(call nrc.fuzzy.FuzzyValue setMatchThreshold 0.1)

; Rule 1 - Initialize Global Variables
(defrule MAIN::init-FuzzyVariables
    (declare (salience 101))
    (initial-fact)
    ?Data <- (Data (id ?id))
    =>
    (?*rsi* addTerm "Low" (new ZFuzzySet 0 20))
    (?*rsi* addTerm "Medium" (new TrapezoidFuzzySet 10 20 50 70))
    (?*rsi* addTerm "High" (new SFuzzySet 70 100))
    
    (?*so* addTerm "Low" (new ZFuzzySet 0 20))
    (?*so* addTerm "Medium" (new TrapezoidFuzzySet 10 20 50 70))
    (?*so* addTerm "High" (new SFuzzySet 70 100))
    
    (assert (Data_rsi (new FuzzyValue ?*rsi* (new SingletonFuzzySet (fact-slot-value ?Data rsi)))))
    (assert (Data_so (new FuzzyValue ?*so* (new SingletonFuzzySet (fact-slot-value ?Data so)))))
)
; Rules for MACD
(defrule macdlow
   (declare (no-loop TRUE)(salience 99))
   ?Data <- (Data {macd < macd_trigger} (macd_trigger ?macd_trigger))
=>
   (printout t "MACD : "(fact-slot-value ?Data macd) " is < MACD Trigger : " ?macd_trigger ". Hence, MACD is Low"crlf)
   (bind ?*macd_flag* "Low")
)
(defrule macdhigh
   (declare (no-loop TRUE)(salience 98))
   ?Data <- (Data {macd > macd_trigger} (macd_trigger ?macd_trigger))
=>
   (printout t "MACD : "(fact-slot-value ?Data macd) " is > MACD Trigger : " ?macd_trigger ". Hence, MACD is High"crlf)
   (bind ?*macd_flag* "High")
)

; Rules for RSI
(defrule rsi_high
   ;(declare (no-loop TRUE)(salience 97))
   (Data_rsi ?Data&:(fuzzy-match ?Data "High"))
=>
   (printout t "RSI : " Data_rsi ?*rsi* " is Overbought implies RSI is High" crlf)
   (bind ?*rsi_flag* "High")
)
(defrule rsi_med
   ;(declare (no-loop TRUE)(salience 96))
   (Data_rsi ?Data&:(fuzzy-match ?Data "Medium"))
=>
   (printout t "RSI : " Data_rsi ?*rsi* " is Normal implies RSI is Medium" crlf) 
   (bind ?*rsi_flag* "Medium") 
)
(defrule rsi_low
   ;(declare (no-loop TRUE)(salience 95))
   (Data_rsi ?Data&:(fuzzy-match ?Data "Low"))
=>
   (bind ?*rsi_flag* "Low")
   (printout t "RSI : " Data_rsi ?*rsi* " is Oversold implies RSI is Low" crlf)  
   
)

; Rules for SO
(defrule so_high
   ;(declare (no-loop TRUE)(salience 94))
   (Data_so ?Data&:(fuzzy-match ?Data "High"))
=>
   (bind ?*so_flag* "High")
   (printout t "SO : " Data_so ?*so* " is Overbought implies SO is High" crlf) 
   
)
(defrule so_med
   ;(declare (no-loop TRUE)(salience 93))
   (Data_so ?Data&:(fuzzy-match ?Data "Medium"))
=>
   (printout t "SO : " Data_so ?*so* " is Normal implies SO is Medium" crlf)
   (bind ?*so_flag* "Medium")  
)
(defrule so_low
   ;(declare (no-loop TRUE)(salience 92))
   (Data_so ?Data&:(fuzzy-match ?Data "Low"))
=>
   (printout t "SO : " Data_so ?*so* " is Oversold implies SO is Low" crlf)
   (bind ?*so_flag* "Low")  
)

(defrule setObv
   (declare (no-loop TRUE)(salience 100))
   ?Data <- (Data (id ?id))
=>
   (bind ?*obv_flag* ?Data.obv)
   (printout t "On-Balance Volume:" ?Data.obv crlf)
)

(defrule buy1
   (declare (no-loop TRUE)(salience 91))
   ?Data <- (Data (id ?id))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Low"))(and(eq ?*so_flag* "Low")(eq ?*obv_flag* "High"))) then
        (printout t "Rule 1 : Triggered" crlf)
        (modify ?Data (result "Buy"))
    )
)

(defrule buy2
   (declare (no-loop TRUE)(salience 90))
   ?Data <- (Data (id ?id))
=>
  (if (and(and(eq ?*macd_flag* "Low")(eq ?*rsi_flag* "High"))(and(eq ?*so_flag* "High")(eq ?*obv_flag* "Low"))) then
        (printout t "Rule 2 : Triggered" crlf)
        (modify ?Data (result "Buy"))
    )
)

(defrule buy3
   (declare (no-loop TRUE)(salience 89))
   ?Data <- (Data (id ?id))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Medium"))(and(eq ?*so_flag* "Medium")(eq ?*obv_flag* "High"))) then
        (printout t "Rule 3 : Triggered" crlf)
        (modify ?Data (result "Buy"))
    )
)

(defrule sell1
   (declare (no-loop TRUE)(salience 88))
   ?Data <- (Data (id ?id))
=>
  (if (and(and(eq ?*macd_flag* "Low")(eq ?*rsi_flag* "Medium"))(and(eq ?*so_flag* "High")(eq ?*obv_flag* "Low"))) then
        (printout t "Rule 4 : Triggered" crlf)
        (modify ?Data (result "Sell"))
    )
)

(defrule hold1
   (declare (no-loop TRUE)(salience 87))
   ?Data <- (Data (id ?id))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Medium"))(and(eq ?*so_flag* "Medium")(eq ?*obv_flag* "Low"))) then
        (printout t "Rule 5 : Triggered" crlf)
        (modify ?Data (result "Hold"))
    )
)

(defrule sell2
   (declare (no-loop TRUE)(salience 86))
   ?Data <- (Data (id ?id))
=>
  (if (and(eq ?*rsi_flag* "High")(and(eq ?*so_flag* "High")(eq ?*obv_flag* "Low"))) then
        (printout t "Rule 6 : Triggered" crlf)
        (modify ?Data (result "Sell"))
    )
)

(defrule hold2
   (declare (no-loop TRUE)(salience 85))
   ?Data <- (Data (id ?id))
=>
  (if (and(and(eq ?*macd_flag* "Low")(eq ?*rsi_flag* "Medium"))(eq ?*so_flag* "Medium")) then
        (printout t "Rule 7 : Triggered" crlf)
        (modify ?Data (result "Hold"))
    )
)
(reset)
(defrule init
    (declare (salience 50))
=>       
(assert (Data
            (id "name")
            (macd 6)
            (macd_trigger 12)
            (obv "Low")
            (rsi 95)
            (so 95)           
            (result "")
            )
) )  

(run)