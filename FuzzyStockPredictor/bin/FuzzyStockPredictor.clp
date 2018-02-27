(import nrc.fuzzy.*)

(import nrc.fuzz.jess.*)

(load-package nrc.fuzzy.jess.FuzzyFunctions)

(deftemplate Data (declare (from-class indicatorsCalculations.Data)))
(deftemplate Data_rsi
    "Auto-generated"
	(declare (ordered TRUE)))
(deftemplate Data_so
    "Auto-generated"
	(declare (ordered TRUE)))

(defglobal ?*rsi* = (new FuzzyVariable "rsi" 0 100))
(defglobal ?*so* = (new FuzzyVariable "so" 0 100))

(defglobal ?*macd_flag* = "")
(defglobal ?*rsi_flag* = "")
(defglobal ?*so_flag* = "")
(defglobal ?*obv_flag* = "")

(defglobal ?*macd_res* = "")
(defglobal ?*rsi_res* = "")
(defglobal ?*so_res* = "")

(call nrc.fuzzy.FuzzyValue setMatchThreshold 0.1)

; Rule 1 - Initialize Global Variables
(defrule MAIN::init-FuzzyVariables
    (declare (salience 101))
    (initial-fact)
    =>
    (?*rsi* addTerm "Low" (new ZFuzzySet 0 20))
    (?*rsi* addTerm "Medium" (new PIFuzzySet 20 50))
    (?*rsi* addTerm "High" (new SFuzzySet 70 100))
    
    (?*so* addTerm "Low" (new ZFuzzySet 0 20))
    (?*so* addTerm "Medium" (new PIFuzzySet 30 50))
    (?*so* addTerm "High" (new SFuzzySet 80 100))
    
)
; Rules for MACD
(defrule MAIN::macdlow
   (declare (no-loop TRUE)(salience 99))
   ?Data <- (Data {macd < macd_trigger} (macd_trigger ?macd_trigger))
=>
   (bind ?*macd_res* "MACD is lesser than MACD Trigger. Hence, MACD is Low. ( MACD is ")
   (bind ?*macd_flag* "Low")
)
(defrule macdhigh
   (declare (no-loop TRUE)(salience 98))
   ?Data <- (Data {macd > macd_trigger} (macd_trigger ?macd_trigger))
=>
   (bind ?*macd_res* "MACD is greater than MACD Trigger. Hence, MACD is High. ( MACD is ")
   (bind ?*macd_flag* "High")
)

; Rules for RSI
(defrule rsi_high
   (declare (no-loop TRUE)(salience 97))
   ?Data <- (Data {rsi > 70} (rsi ?rsi))
=>
   (bind ?*rsi_res* "RSI stock is overbought. Hence, RSI is High. ( RSI is ")  
   (bind ?*rsi_flag* "High")
)
(defrule rsi_med
   (declare (no-loop TRUE)(salience 96))
   ?Data <- (Data {rsi > 30}{rsi < 70} (rsi ?rsi))
=>
   (bind ?*rsi_res* "RSI stock is normal(neither overbought nor oversold) implying RSI of Medium level. ( RSI is ") 
   (bind ?*rsi_flag* "Medium") 
)
(defrule rsi_low
   (declare (no-loop TRUE)(salience 95))
   ?Data <- (Data {rsi < 30} (rsi ?rsi))
=>
   (bind ?*rsi_res* "RSI stock is oversold. Hence, RSI is Low. ( RSI is ")  
   (bind ?*rsi_flag* "Low")
)

; Rules for SO
(defrule so_high
   (declare (no-loop TRUE)(salience 94))
   ?Data <- (Data {so > 80} (so ?so))
=>
   (bind ?*so_res* "SO stock is overbought. Hence, SO is High. ( SO is ") 
   (bind ?*so_flag* "High")
)
(defrule so_med
   (declare (no-loop TRUE)(salience 93))
   ?Data <- (Data {so > 20}{so < 80} (so ?so))
=>
   (bind ?*so_res* "SO stock is normal implying Medium SO level. ( SO is ")
   (bind ?*so_flag* "Medium")  
)
(defrule so_low
   (declare (no-loop TRUE)(salience 92))
   ?Data <- (Data {so < 20} (so ?so))
=>
   (bind ?*so_res* "SO stock is oversold. Hence, SO is Low. ( SO is ")
   (bind ?*so_flag* "Low")  
)

(defrule setObv
   (declare (no-loop TRUE)(salience 100))
   ?Data <- (Data (obv ?obv))
=>
   (bind ?*obv_flag* ?obv)
)

(defrule buy1
   (declare (no-loop TRUE)(salience 91))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Low"))(and(eq ?*so_flag* "Low")(eq ?*obv_flag* "High"))) then
        (modify ?Data (result "Buy"))
    )
)

(defrule buy2
   (declare (no-loop TRUE)(salience 90))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "Low")(eq ?*rsi_flag* "High"))(and(eq ?*so_flag* "High")(eq ?*obv_flag* "Low"))) then
        (modify ?Data (result "Buy"))
    )
)

(defrule buy3
   (declare (no-loop TRUE)(salience 89))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Medium"))(and(eq ?*so_flag* "Medium")(eq ?*obv_flag* "High"))) then
        (modify ?Data (result "Buy"))
    )
)

(defrule sell1
   (declare (no-loop TRUE)(salience 88))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "Low")(eq ?*rsi_flag* "Medium"))(and(eq ?*so_flag* "High")(eq ?*obv_flag* "Low"))) then
        (modify ?Data (result "Sell"))
    )
)

(defrule hold1
   (declare (no-loop TRUE)(salience 87))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Medium"))(and(eq ?*so_flag* "Medium")(eq ?*obv_flag* "Low"))) then
        (modify ?Data (result "Hold"))
    )
)

(defrule sell2
   (declare (no-loop TRUE)(salience 86))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(eq ?*rsi_flag* "High")(and(eq ?*so_flag* "High")(eq ?*obv_flag* "Low"))) then
        (modify ?Data (result "Sell"))
    )
)

(defrule hold2
   (declare (no-loop TRUE)(salience 85))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "Low")(eq ?*rsi_flag* "Medium"))(eq ?*so_flag* "Medium")) then
        (modify ?Data (result "Hold"))
    )
    
)

(defrule sell3
   (declare (no-loop TRUE)(salience 85))
   ?Data <- (Data (obv ?obv))
=>
  (if (and(and(eq ?*macd_flag* "High")(eq ?*rsi_flag* "Medium"))(eq ?*so_flag* "High")) then
        (modify ?Data (result "Sell"))
    )
)

(defrule printResult
   (declare (no-loop TRUE)(salience 50))
   ?Data <- (Data (obv ?obv))
=>
   (printout t ?*macd_res* (fact-slot-value ?Data macd) ". MACD trigger is " (fact-slot-value ?Data macd_trigger) " )" crlf)
   (printout t ?*rsi_res* (fact-slot-value ?Data rsi) " )" crlf)
   (printout t ?*so_res* (fact-slot-value ?Data so) " )" crlf)
)